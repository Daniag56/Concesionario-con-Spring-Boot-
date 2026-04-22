package com.example.concesionario.controller;

import com.example.concesionario.entity.Extra;
import com.example.concesionario.repository.ExtraRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/extras")
public class ExtraWebController {

    private final ExtraRepository extraRepository;

    public ExtraWebController(ExtraRepository extraRepository) {
        this.extraRepository = extraRepository;
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("listaExtras", extraRepository.findAll());
        return "extras/lista";
    }

    @GetMapping({"/crear", "/nuevo"})
    public String crear(Model model) {
        model.addAttribute("titulo", "Nuevo Extra");
        model.addAttribute("extra", new Extra());
        model.addAttribute("accion", "/extras/guardar");
        return "extras/form";
    }

    @PostMapping("/guardar")
    public String guardar(Extra extra) {
        extraRepository.save(extra);
        return "redirect:/extras/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("titulo", "Editar Extra");
        model.addAttribute("extra", extraRepository.findById(id).orElseThrow());
        model.addAttribute("accion", "/extras/editar/" + id);
        return "extras/form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Extra actualizado) {
        Extra extra = extraRepository.findById(id).orElseThrow();
        extra.setNombre(actualizado.getNombre());
        extra.setDescripcion(actualizado.getDescripcion());
        extra.setPrecioAdicional(actualizado.getPrecioAdicional());
        extraRepository.save(extra);
        return "redirect:/extras/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        extraRepository.deleteById(id);
        return "redirect:/extras/lista";
    }
}