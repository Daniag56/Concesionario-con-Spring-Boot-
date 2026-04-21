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
    public String listaExtras(Model model) {
        model.addAttribute("listaExtras", extraRepository.findAll());
        return "extras-lista";
    }

    @GetMapping("/nuevo")
    public String nuevoExtra(Model model) {
        model.addAttribute("extra", new Extra());
        return "extra-form";
    }

    @PostMapping("/guardar")
    public String guardarExtra(Extra extra) {
        extraRepository.save(extra);
        return "redirect:/extras/lista";
    }

    @GetMapping("/editar/{id}")
    public String editarExtra(@PathVariable Long id, Model model) {
        model.addAttribute("extra", extraRepository.findById(id).orElse(null));
        return "extra-form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Extra actualizado) {

        Extra extra = extraRepository.findById(id).orElse(null);

        if (extra != null) {
            extra.setNombre(actualizado.getNombre());
            extra.setDescripcion(actualizado.getDescripcion());
            extra.setPrecioAdicional(actualizado.getPrecioAdicional());
            extraRepository.save(extra);
        }

        return "redirect:/extras/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarExtra(@PathVariable Long id) {
        extraRepository.deleteById(id);
        return "redirect:/extras/lista";
    }
}

