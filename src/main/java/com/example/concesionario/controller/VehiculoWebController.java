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

import java.util.List;

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

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("listaVehiculos", vehiculoRepository.findAll());
        return "vehiculos/lista";
    }

    @GetMapping({"/crear", "/nuevo"})
    public String crear(Model model) {
        model.addAttribute("titulo", "Nuevo Vehículo");
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("modelos", modeloRepository.findAll());
        model.addAttribute("concesionarios", concesionarioRepository.findAll());
        model.addAttribute("extras", extraRepository.findAll());
        model.addAttribute("accion", "/vehiculos/guardar");
        return "vehiculos/form";
    }

    @PostMapping("/guardar")
    public String guardar(Vehiculo vehiculo) {
        resolverRelaciones(vehiculo);
        vehiculoRepository.save(vehiculo);
        return "redirect:/vehiculos/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("titulo", "Editar Vehículo");
        model.addAttribute("vehiculo", vehiculoRepository.findById(id).orElseThrow());
        model.addAttribute("modelos", modeloRepository.findAll());
        model.addAttribute("concesionarios", concesionarioRepository.findAll());
        model.addAttribute("extras", extraRepository.findAll());
        model.addAttribute("accion", "/vehiculos/editar/" + id);
        return "vehiculos/form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Vehiculo actualizado) {
        Vehiculo vehiculo = vehiculoRepository.findById(id).orElseThrow();

        vehiculo.setCodigoVehiculo(actualizado.getCodigoVehiculo());
        vehiculo.setVin(actualizado.getVin());
        vehiculo.setMatricula(actualizado.getMatricula());
        vehiculo.setFechaFabricacion(actualizado.getFechaFabricacion());
        vehiculo.setKilometraje(actualizado.getKilometraje());
        vehiculo.setColor(actualizado.getColor());
        vehiculo.setPrecioVenta(actualizado.getPrecioVenta());
        vehiculo.setEstado(actualizado.getEstado());
        vehiculo.setCombustible(actualizado.getCombustible());

        resolverRelacionesEnEdicion(actualizado, vehiculo);
        vehiculoRepository.save(vehiculo);
        return "redirect:/vehiculos/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
        return "redirect:/vehiculos/lista";
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private void resolverRelaciones(Vehiculo v) {
        if (v.getModelo() != null && v.getModelo().getId() != null) {
            v.setModelo(modeloRepository.findById(v.getModelo().getId()).orElseThrow());
        }
        if (v.getConcesionario() != null && v.getConcesionario().getId() != null) {
            v.setConcesionario(concesionarioRepository.findById(v.getConcesionario().getId()).orElseThrow());
        }
        if (v.getExtras() != null) {
            List<Extra> extras = v.getExtras().stream()
                    .map(e -> extraRepository.findById(e.getId()).orElseThrow())
                    .toList();
            v.setExtras(extras);
        }
    }

    private void resolverRelacionesEnEdicion(Vehiculo origen, Vehiculo destino) {
        if (origen.getModelo() != null && origen.getModelo().getId() != null) {
            destino.setModelo(modeloRepository.findById(origen.getModelo().getId()).orElseThrow());
        }
        if (origen.getConcesionario() != null && origen.getConcesionario().getId() != null) {
            destino.setConcesionario(concesionarioRepository.findById(origen.getConcesionario().getId()).orElseThrow());
        }
        if (origen.getExtras() != null) {
            List<Extra> extras = origen.getExtras().stream()
                    .map(e -> extraRepository.findById(e.getId()).orElseThrow())
                    .toList();
            destino.setExtras(extras);
        }
    }
}