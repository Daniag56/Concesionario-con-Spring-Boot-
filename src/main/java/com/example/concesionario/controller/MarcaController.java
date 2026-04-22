package com.example.concesionario.controller;

import com.example.concesionario.entity.Marca;
import com.example.concesionario.repository.MarcaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/marcas")
public class MarcaController {

    private final MarcaRepository marcaRepository;

    public MarcaController(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("marcas", marcaRepository.findAll());
        return "marcas/lista";
    }

    @GetMapping({"/crear", "/nuevo"})
    public String crear(Model model) {
        model.addAttribute("titulo", "Nueva Marca");
        model.addAttribute("marca", new Marca());
        model.addAttribute("accion", "/marcas/guardar");
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
        model.addAttribute("accion", "/marcas/editar/" + id);
        return "marcas/form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, @ModelAttribute Marca actualizado) {
        Marca marca = marcaRepository.findById(id).orElseThrow();
        marca.setNombre(actualizado.getNombre());
        marca.setPaisOrigen(actualizado.getPaisOrigen());
        marca.setAnioFundacion(actualizado.getAnioFundacion());
        marcaRepository.save(marca);
        return "redirect:/marcas/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        marcaRepository.deleteById(id);
        return "redirect:/marcas/lista";
    }
}