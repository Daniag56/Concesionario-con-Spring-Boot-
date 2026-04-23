package com.example.concesionario.controller;

import com.example.concesionario.entity.Extra;
import com.example.concesionario.entity.Vehiculo;
import com.example.concesionario.repository.ConcesionarioRepository;
import com.example.concesionario.repository.ExtraRepository;
import com.example.concesionario.repository.ModeloRepository;
import com.example.concesionario.repository.VehiculoRepository;
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
        this.vehiculoRepository    = vehiculoRepository;
        this.modeloRepository      = modeloRepository;
        this.concesionarioRepository = concesionarioRepository;
        this.extraRepository       = extraRepository;
    }

    // ── LISTA ────────────────────────────────────────────────────────────────

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("listaVehiculos", vehiculoRepository.findAll());
        return "vehiculos/lista";
    }

    // ── CREAR ────────────────────────────────────────────────────────────────

    @GetMapping({"/crear", "/nuevo"})
    public String crear(Model model) {
        model.addAttribute("titulo",   "Nuevo Vehículo");
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("accion",   "/vehiculos/guardar");
        poblarSelectores(model);
        return "vehiculos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Vehiculo vehiculo, Model model) {
        try {
            resolverRelaciones(vehiculo);
            vehiculoRepository.save(vehiculo);
            return "redirect:/vehiculos/lista";
        } catch (Exception ex) {
            // Si algo falla, volvemos al form con las listas disponibles
            model.addAttribute("titulo",   "Nuevo Vehículo");
            model.addAttribute("vehiculo", vehiculo);
            model.addAttribute("accion",   "/vehiculos/guardar");
            model.addAttribute("error",    ex.getMessage());
            poblarSelectores(model);
            return "vehiculos/form";
        }
    }

    // ── EDITAR ───────────────────────────────────────────────────────────────

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("titulo",   "Editar Vehículo");
        model.addAttribute("vehiculo", vehiculoRepository.findById(id).orElseThrow());
        model.addAttribute("accion",   "/vehiculos/editar/" + id);
        poblarSelectores(model);
        return "vehiculos/form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id,
                                 @ModelAttribute Vehiculo actualizado,
                                 Model model) {
        try {
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
        } catch (Exception ex) {
            model.addAttribute("titulo",   "Editar Vehículo");
            model.addAttribute("vehiculo", actualizado);
            model.addAttribute("accion",   "/vehiculos/editar/" + id);
            model.addAttribute("error",    ex.getMessage());
            poblarSelectores(model);
            return "vehiculos/form";
        }
    }

    // ── ELIMINAR ─────────────────────────────────────────────────────────────

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
        return "redirect:/vehiculos/lista";
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────

    /** Añade al Model las listas necesarias para los <select> del formulario */
    private void poblarSelectores(Model model) {
        model.addAttribute("modelos",        modeloRepository.findAll());
        model.addAttribute("concesionarios", concesionarioRepository.findAll());
        model.addAttribute("extras",         extraRepository.findAll());
    }

    private void resolverRelaciones(Vehiculo v) {
        if (v.getModelo() != null && v.getModelo().getId() != null) {
            v.setModelo(modeloRepository.findById(v.getModelo().getId()).orElseThrow());
        }
        if (v.getConcesionario() != null && v.getConcesionario().getId() != null) {
            v.setConcesionario(concesionarioRepository.findById(v.getConcesionario().getId()).orElseThrow());
        }
        if (v.getExtras() != null && !v.getExtras().isEmpty()) {
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
        if (origen.getExtras() != null && !origen.getExtras().isEmpty()) {
            List<Extra> extras = origen.getExtras().stream()
                    .map(e -> extraRepository.findById(e.getId()).orElseThrow())
                    .toList();
            destino.setExtras(extras);
        }
    }
}