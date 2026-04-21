package com.example.concesionario.controller;

import com.example.concesionario.repository.MarcaRepository;
import com.example.concesionario.repository.ModeloRepository;
import com.example.concesionario.repository.ConcesionarioRepository;
import com.example.concesionario.repository.ExtraRepository;
import com.example.concesionario.repository.VehiculoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador REST encargado de proporcionar datos resumidos
 * para el panel de control (dashboard) de la aplicación.
 *
 * <p>Este controlador expone un endpoint que devuelve un resumen
 * con el número total de marcas, modelos, concesionarios, extras
 * y vehículos registrados en la base de datos.</p>
 *
 * <p>Se utiliza principalmente para integraciones AJAX o clientes externos
 * que necesiten obtener estadísticas globales.</p>
 */
@RestController
public class DashboardController {

    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final ConcesionarioRepository concesionarioRepository;
    private final ExtraRepository extraRepository;
    private final VehiculoRepository vehiculoRepository;

    /**
     * Constructor que inyecta los repositorios necesarios para obtener
     * los datos del resumen.
     */
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

    /**
     * Devuelve un resumen con el número total de entidades del sistema.
     *
     * @return JSON con:
     *  - marcas
     *  - modelos
     *  - concesionarios
     *  - extras
     *  - vehículos
     */
    @GetMapping("/api/dashboard/resumen")
    public ResponseEntity<Map<String, Long>> resumen() {

        Map<String, Long> datos = new LinkedHashMap<>();
        datos.put("marcas", marcaRepository.count());
        datos.put("modelos", modeloRepository.count());
        datos.put("concesionarios", concesionarioRepository.count());
        datos.put("extras", extraRepository.count());
        datos.put("vehiculos", vehiculoRepository.count());

        return ResponseEntity.ok(datos);
    }
}

