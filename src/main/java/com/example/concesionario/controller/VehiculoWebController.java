package com.example.concesionario.controller;

import com.example.concesionario.entity.Vehiculo;
import com.example.concesionario.entity.Modelo;
import com.example.concesionario.entity.Concesionario;
import com.example.concesionario.entity.Extra;
import com.example.concesionario.repository.VehiculoRepository;
import com.example.concesionario.repository.ModeloRepository;
import com.example.concesionario.repository.ConcesionarioRepository;
import com.example.concesionario.repository.ExtraRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoWebController {

    private final VehiculoRepository vehiculoRepository;
    private final ModeloRepository modeloRepository;
    private final ConcesionarioRepository concesionarioRepository;
    private final ExtraRepository extraRepository;

    public VehiculoWebController(VehiculoRepository vehiculoRepository,
                                 ModeloRepository modeloRepository,
                                 ConcesionarioRepository concesionarioRepository,
                                 ExtraRepository extraRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.modeloRepository = modeloRepository;
        this.concesionarioRepository = concesionarioRepository;
        this.extraRepository = extraRepository;
    }

    // LISTA
    @GetMapping("/lista")
    public String listaVehiculos(Model model) {
        model.addAttribute("listaVehiculos", vehiculoRepository.findAll());
        return "vehiculos-lista";
    }

    // NUEVO
    @GetMapping("/nuevo")
    public String nuevoVehiculo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("modelos", modeloRepository.findAll());
        model.addAttribute("concesionarios", concesionarioRepository.findAll());
        model.addAttribute("extras", extraRepository.findAll());
        return "vehiculo-form";
    }

    // GUARDAR NUEVO
    @PostMapping("/guardar")
    public String guardarVehiculo(Vehiculo vehiculo) {

        // Modelo
        if (vehiculo.getModelo() != null && vehiculo.getModelo().getId() != null) {
            Modelo modeloReal = modeloRepository.findById(vehiculo.getModelo().getId()).orElse(null);
            vehiculo.setModelo(modeloReal);
        }

        // Concesionario
        if (vehiculo.getConcesionario() != null && vehiculo.getConcesionario().getId() != null) {
            Concesionario concesionarioReal = concesionarioRepository.findById(vehiculo.getConcesionario().getId()).orElse(null);
            vehiculo.setConcesionario(concesionarioReal);
        }

        // Extras (lista)
        if (vehiculo.getExtras() != null) {
            vehiculo.setExtras(
                    vehiculo.getExtras().stream()
                            .map(e -> extraRepository.findById(e.getId()).orElse(null))
                            .toList()
            );
        }

        vehiculoRepository.save(vehiculo);
        return "redirect:/vehiculos/lista";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editarVehiculo(@PathVariable Long id, Model model) {
        Vehiculo vehiculo = vehiculoRepository.findById(id).orElse(null);

        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("modelos", modeloRepository.findAll());
        model.addAttribute("concesionarios", concesionarioRepository.findAll());
        model.addAttribute("extras", extraRepository.findAll());

        return "vehiculo-form";
    }

    // GUARDAR EDICIÓN
    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Vehiculo vehiculoActualizado) {

        Vehiculo vehiculo = vehiculoRepository.findById(id).orElse(null);

        if (vehiculo != null) {

            vehiculo.setCodigoVehiculo(vehiculoActualizado.getCodigoVehiculo());
            vehiculo.setVin(vehiculoActualizado.getVin());
            vehiculo.setMatricula(vehiculoActualizado.getMatricula());
            vehiculo.setFechaFabricacion(vehiculoActualizado.getFechaFabricacion());
            vehiculo.setKilometraje(vehiculoActualizado.getKilometraje());
            vehiculo.setColor(vehiculoActualizado.getColor());
            vehiculo.setPrecioVenta(vehiculoActualizado.getPrecioVenta());
            vehiculo.setEstado(vehiculoActualizado.getEstado());
            vehiculo.setCombustible(vehiculoActualizado.getCombustible());

            // Modelo
            if (vehiculoActualizado.getModelo() != null && vehiculoActualizado.getModelo().getId() != null) {
                Modelo modeloReal = modeloRepository.findById(vehiculoActualizado.getModelo().getId()).orElse(null);
                vehiculo.setModelo(modeloReal);
            }

            // Concesionario
            if (vehiculoActualizado.getConcesionario() != null && vehiculoActualizado.getConcesionario().getId() != null) {
                Concesionario concesionarioReal = concesionarioRepository.findById(vehiculoActualizado.getConcesionario().getId()).orElse(null);
                vehiculo.setConcesionario(concesionarioReal);
            }

            // Extras
            if (vehiculoActualizado.getExtras() != null) {
                vehiculo.setExtras(
                        vehiculoActualizado.getExtras().stream()
                                .map(e -> extraRepository.findById(e.getId()).orElse(null))
                                .toList()
                );
            }

            vehiculoRepository.save(vehiculo);
        }

        return "redirect:/vehiculos/lista";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
        return "redirect:/vehiculos/lista";
    }
}
