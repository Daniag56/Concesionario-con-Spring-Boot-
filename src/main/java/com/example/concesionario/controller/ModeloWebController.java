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

    public ModeloWebController(ModeloRepository modeloRepository,
                               MarcaRepository marcaRepository) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
    }

    @GetMapping("/lista")
    public String listaModelos(Model model) {
        model.addAttribute("listaModelos", modeloRepository.findAll());
        return "modelos-lista";
    }

    @GetMapping("/nuevo")
    public String nuevoModelo(Model model) {
        model.addAttribute("modelo", new Modelo());
        model.addAttribute("marcas", marcaRepository.findAll());
        return "modelo-form";
    }

    @PostMapping("/guardar")
    public String guardarModelo(Modelo modelo) {

        if (modelo.getMarca() != null && modelo.getMarca().getId() != null) {
            Marca marcaReal = marcaRepository.findById(modelo.getMarca().getId()).orElse(null);
            modelo.setMarca(marcaReal);
        }

        modeloRepository.save(modelo);
        return "redirect:/modelos/lista";
    }

    @GetMapping("/editar/{id}")
    public String editarModelo(@PathVariable Long id, Model model) {
        model.addAttribute("modelo", modeloRepository.findById(id).orElse(null));
        model.addAttribute("marcas", marcaRepository.findAll());
        return "modelo-form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, Modelo modeloActualizado) {

        Modelo modelo = modeloRepository.findById(id).orElse(null);

        if (modelo != null) {
            modelo.setNombre(modeloActualizado.getNombre());
            modelo.setAnioLanzamiento(modeloActualizado.getAnioLanzamiento());
            modelo.setNumeroPlazas(modeloActualizado.getNumeroPlazas());
            modelo.setTipoCarroceria(modeloActualizado.getTipoCarroceria());
            modelo.setPotenciaCv(modeloActualizado.getPotenciaCv());

            if (modeloActualizado.getMarca() != null && modeloActualizado.getMarca().getId() != null) {
                Marca marcaReal = marcaRepository.findById(modeloActualizado.getMarca().getId()).orElse(null);
                modelo.setMarca(marcaReal);
            }

            modeloRepository.save(modelo);
        }

        return "redirect:/modelos/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarModelo(@PathVariable Long id) {
        modeloRepository.deleteById(id);
        return "redirect:/modelos/lista";
    }
}
