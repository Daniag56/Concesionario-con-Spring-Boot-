package com.example.concesionario.controller;

import com.example.concesionario.entity.Marca;
import com.example.concesionario.entity.Modelo;
import com.example.concesionario.repository.MarcaRepository;
import com.example.concesionario.repository.ModeloRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/marcas")
public class MarcaController {

    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;

    public MarcaController(MarcaRepository marcaRepository, ModeloRepository modeloRepository) {
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
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
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            List<Modelo> modelos = modeloRepository.findByMarcaId(id);
            if (!modelos.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No se puede eliminar la marca porque tiene modelos asociados.");
                return "redirect:/marcas/lista";
            }
            marcaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Marca eliminada correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar este registro porque está siendo usado por otros datos.");
        }
        return "redirect:/marcas/lista";
    }
}