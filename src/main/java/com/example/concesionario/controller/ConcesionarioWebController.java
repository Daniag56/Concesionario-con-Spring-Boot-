package com.example.concesionario.controller;

import com.example.concesionario.entity.Concesionario;
import com.example.concesionario.repository.ConcesionarioRepository;
import com.example.concesionario.repository.VehiculoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/concesionarios")
public class ConcesionarioWebController {

    private final ConcesionarioRepository concessionarioRepository;
    private final VehiculoRepository vehiculoRepository;

    public ConcesionarioWebController(ConcesionarioRepository concessionarioRepository,
                                    VehiculoRepository vehiculoRepository) {
        this.concessionarioRepository = concessionarioRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("listaConcesionarios", concessionarioRepository.findAll());
        return "concesionarios/lista";
    }

    @GetMapping({"/crear", "/nuevo"})
    public String crear(Model model) {
        model.addAttribute("titulo", "Nuevo Concesionario");
        model.addAttribute("concesionario", new Concesionario());
        model.addAttribute("accion", "/concesionarios/guardar");
        return "concesionarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(Concesionario concessionario) {
        concessionarioRepository.save(concessionario);
        return "redirect:/concesionarios/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("titulo", "Editar Concesionario");
        model.addAttribute("concesionario", concessionarioRepository.findById(id).orElseThrow());
        model.addAttribute("accion", "/concesionarios/editar/" + id);
        return "concesionarios/form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Concesionario actualizado) {
        Concesionario c = concessionarioRepository.findById(id).orElseThrow();
        c.setNombreComercial(actualizado.getNombreComercial());
        c.setCiudad(actualizado.getCiudad());
        c.setDireccion(actualizado.getDireccion());
        c.setPlazasExposicion(actualizado.getPlazasExposicion());
        c.setMetrosCuadrados(actualizado.getMetrosCuadrados());
        concessionarioRepository.save(c);
        return "redirect:/concesionarios/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var vehiculos = vehiculoRepository.findByConcesionarioId(id);
            if (!vehiculos.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No se puede eliminar el concessionario porque tiene vehículos asociados.");
                return "redirect:/concesionarios/lista";
            }
            concessionarioRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Concessionario eliminado correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar este registro porque está siendo usado por otros datos.");
        }
        return "redirect:/concesionarios/lista";
    }
}