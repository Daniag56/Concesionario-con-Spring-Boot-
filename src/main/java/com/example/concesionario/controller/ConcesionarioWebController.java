package com.example.concesionario.controller;

import com.example.concesionario.entity.Concesionario;
import com.example.concesionario.repository.ConcesionarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/concesionarios")
public class ConcesionarioWebController {

    private final ConcesionarioRepository concesionarioRepository;

    public ConcesionarioWebController(ConcesionarioRepository concesionarioRepository) {
        this.concesionarioRepository = concesionarioRepository;
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("listaConcesionarios", concesionarioRepository.findAll());
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
    public String guardar(Concesionario concesionario) {
        concesionarioRepository.save(concesionario);
        return "redirect:/concesionarios/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("titulo", "Editar Concesionario");
        model.addAttribute("concesionario", concesionarioRepository.findById(id).orElseThrow());
        model.addAttribute("accion", "/concesionarios/editar/" + id);
        return "concesionarios/form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Concesionario actualizado) {
        Concesionario c = concesionarioRepository.findById(id).orElseThrow();
        c.setNombreComercial(actualizado.getNombreComercial());
        c.setCiudad(actualizado.getCiudad());
        c.setDireccion(actualizado.getDireccion());
        c.setPlazasExposicion(actualizado.getPlazasExposicion());
        c.setMetrosCuadrados(actualizado.getMetrosCuadrados());
        concesionarioRepository.save(c);
        return "redirect:/concesionarios/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        concesionarioRepository.deleteById(id);
        return "redirect:/concesionarios/lista";
    }
}