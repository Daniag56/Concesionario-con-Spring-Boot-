package com.example.concesionario.controller;

import com.example.concesionario.entity.Marca;
import com.example.concesionario.repository.MarcaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/marcas")
public class MarcaWebController {

    private final MarcaRepository marcaRepository;

    public MarcaWebController(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @GetMapping("/lista")
    public String listaMarcas(Model model) {
        model.addAttribute("listaMarcas", marcaRepository.findAll());
        return "marcas-lista";
    }

    @GetMapping("/nuevo")
    public String nuevaMarca(Model model) {
        model.addAttribute("marca", new Marca());
        return "marca-form";
    }

    @PostMapping("/guardar")
    public String guardarMarca(Marca marca) {
        marcaRepository.save(marca);
        return "redirect:/marcas/lista";
    }

    @GetMapping("/editar/{id}")
    public String editarMarca(@PathVariable Long id, Model model) {
        model.addAttribute("marca", marcaRepository.findById(id).orElse(null));
        return "marca-form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Marca marcaActualizada) {
        Marca marca = marcaRepository.findById(id).orElse(null);

        if (marca != null) {
            marca.setNombre(marcaActualizada.getNombre());
            marca.setPaisOrigen(marcaActualizada.getPaisOrigen());
            marca.setAnioFundacion(marcaActualizada.getAnioFundacion());
            marcaRepository.save(marca);
        }

        return "redirect:/marcas/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable Long id) {
        marcaRepository.deleteById(id);
        return "redirect:/marcas/lista";
    }
}

