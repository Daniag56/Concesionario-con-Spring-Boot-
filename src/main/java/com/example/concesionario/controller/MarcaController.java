package com.example.concesionario.controller;

import com.example.concesionario.entity.Marca;
import com.example.concesionario.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/marcas")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de Marcas");
        model.addAttribute("marcas", marcaRepository.findAll());
        return "marcas/lista";
    }

    @GetMapping("/crear")
    public String crear(Model model) {
        model.addAttribute("titulo", "Nueva Marca");
        model.addAttribute("marca", new Marca());
        return "marcas/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Marca marca) {
        marcaRepository.save(marca);
        return "redirect:/marcas/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("titulo", "Editar Marca");
        model.addAttribute("marca", marcaRepository.findById(id).orElseThrow());
        return "marcas/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        marcaRepository.deleteById(id);
        return "redirect:/marcas/lista";
    }
}

