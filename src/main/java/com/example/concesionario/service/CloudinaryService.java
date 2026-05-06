package com.example.concesionario.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;

@Service
public class CloudinaryService {

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    @Value("${cloudinary.folder:concesionario/vehiculos}")
    private String folder;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String uploadVehiculoImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        if (cloudName.isBlank() || apiKey.isBlank() || apiSecret.isBlank()) {
            throw new IllegalStateException("Cloudinary no configurado: revisa cloudinary.cloud-name/api-key/api-secret.");
        }

        try {
            long timestamp = Instant.now().getEpochSecond();
            String signatureBase = "folder=" + folder + "&timestamp=" + timestamp + apiSecret;
            String signature = sha1Hex(signatureBase);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("api_key", apiKey);
            body.add("timestamp", String.valueOf(timestamp));
            body.add("folder", folder);
            body.add("signature", signature);
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename() == null ? "vehiculo.jpg" : file.getOriginalFilename();
                }
            });

            String url = "https://api.cloudinary.com/v1_1/" + cloudName + "/image/upload";
            ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalStateException("No se pudo subir la imagen a Cloudinary.");
            }

            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode secureUrl = json.get("secure_url");
            if (secureUrl == null || secureUrl.asText().isBlank()) {
                throw new IllegalStateException("Cloudinary respondió sin secure_url.");
            }
            return secureUrl.asText();
        } catch (Exception e) {
            throw new IllegalStateException("Error al subir imagen a Cloudinary: " + e.getMessage(), e);
        }
    }

    private String sha1Hex(String value) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
