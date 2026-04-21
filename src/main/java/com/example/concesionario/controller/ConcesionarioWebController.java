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
    public String listaConcesionarios(Model model) {
        model.addAttribute("listaConcesionarios", concesionarioRepository.findAll());
        return "concesionarios-lista";
    }

    @GetMapping("/nuevo")
    public String nuevoConcesionario(Model model) {
        model.addAttribute("concesionario", new Concesionario());
        return "concesionario-form";
    }

    @PostMapping("/guardar")
    public String guardarConcesionario(Concesionario concesionario) {
        concesionarioRepository.save(concesionario);
        return "redirect:/concesionarios/lista";
    }

    @GetMapping("/editar/{id}")
    public String editarConcesionario(@PathVariable Long id, Model model) {
        model.addAttribute("concesionario", concesionarioRepository.findById(id).orElse(null));
        return "concesionario-form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Concesionario actualizado) {

        Concesionario c = concesionarioRepository.findById(id).orElse(null);

        if (c != null) {
            c.setNombreComercial(actualizado.getNombreComercial());
            c.setCiudad(actualizado.getCiudad());
            c.setDireccion(actualizado.getDireccion());
            c.setPlazasExposicion(actualizado.getPlazasExposicion());
            c.setMetrosCuadrados(actualizado.getMetrosCuadrados());
            concesionarioRepository.save(c);
        }

        return "redirect:/concesionarios/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarConcesionario(@PathVariable Long id) {
        concesionarioRepository.deleteById(id);
        return "redirect:/concesionarios/lista";
    }
}

