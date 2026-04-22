package com.example.concesionario.controller;

import com.example.concesionario.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final ConcesionarioRepository concesionarioRepository;
    private final ExtraRepository extraRepository;
    private final VehiculoRepository vehiculoRepository;

    public DashboardController(MarcaRepository marcaRepository,
                               ModeloRepository modeloRepository,
                               ConcesionarioRepository concesionarioRepository,
                               ExtraRepository extraRepository,
                               VehiculoRepository vehiculoRepository) {
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.concesionarioRepository = concesionarioRepository;
        this.extraRepository = extraRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @GetMapping("/resumen")
    public Map<String, Object> resumen() {
        Map<String, Object> data = new HashMap<>();
        data.put("marcas", marcaRepository.count());
        data.put("modelos", modeloRepository.count());
        data.put("concesionarios", concesionarioRepository.count());
        data.put("extras", extraRepository.count());
        data.put("vehiculos", vehiculoRepository.count());
        return data;
    }
}

