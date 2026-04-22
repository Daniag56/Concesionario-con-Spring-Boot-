package com.example.concesionario.controller;

import com.example.concesionario.entity.Modelo;
import com.example.concesionario.entity.Marca;
import com.example.concesionario.repository.ModeloRepository;
import com.example.concesionario.repository.MarcaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/modelos")
public class ModeloWebController {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;

    public ModeloWebController(ModeloRepository modeloRepository, MarcaRepository marcaRepository) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("listaModelos", modeloRepository.findAll());
        return "modelos/lista";
    }

    @GetMapping({"/crear", "/nuevo"})
    public String crear(Model model) {
        model.addAttribute("titulo", "Nuevo Modelo");
        model.addAttribute("modelo", new Modelo());
        model.addAttribute("marcas", marcaRepository.findAll());
        model.addAttribute("accion", "/modelos/guardar");
        return "modelos/form";
    }

    @PostMapping("/guardar")
    public String guardar(Modelo modelo) {
        if (modelo.getMarca() != null && modelo.getMarca().getId() != null) {
            Marca marcaReal = marcaRepository.findById(modelo.getMarca().getId()).orElseThrow();
            modelo.setMarca(marcaReal);
        }
        modeloRepository.save(modelo);
        return "redirect:/modelos/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("titulo", "Editar Modelo");
        model.addAttribute("modelo", modeloRepository.findById(id).orElseThrow());
        model.addAttribute("marcas", marcaRepository.findAll());
        model.addAttribute("accion", "/modelos/editar/" + id);
        return "modelos/form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Modelo actualizado) {
        Modelo modelo = modeloRepository.findById(id).orElseThrow();
        modelo.setNombre(actualizado.getNombre());
        modelo.setAnioLanzamiento(actualizado.getAnioLanzamiento());
        modelo.setNumeroPlazas(actualizado.getNumeroPlazas());
        modelo.setTipoCarroceria(actualizado.getTipoCarroceria());
        modelo.setPotenciaCv(actualizado.getPotenciaCv());
        if (actualizado.getMarca() != null && actualizado.getMarca().getId() != null) {
            modelo.setMarca(marcaRepository.findById(actualizado.getMarca().getId()).orElseThrow());
        }
        modeloRepository.save(modelo);
        return "redirect:/modelos/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        modeloRepository.deleteById(id);
        return "redirect:/modelos/lista";
    }
}