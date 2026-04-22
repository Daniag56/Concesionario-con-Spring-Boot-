package com.example.concesionario.controller;

import com.example.concesionario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired private MarcaRepository marcaRepository;
    @Autowired private ModeloRepository modeloRepository;
    @Autowired private ConcesionarioRepository concesionarioRepository;
    @Autowired private ExtraRepository extraRepository;
    @Autowired private VehiculoRepository vehiculoRepository;

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


