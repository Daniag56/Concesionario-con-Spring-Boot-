package com.example.concesionario.controller;

import com.example.concesionario.entity.Extra;
import com.example.concesionario.entity.Marca;
import com.example.concesionario.entity.Modelo;
import com.example.concesionario.entity.Vehiculo;
import com.example.concesionario.repository.ConcesionarioRepository;
import com.example.concesionario.repository.ExtraRepository;
import com.example.concesionario.repository.MarcaRepository;
import com.example.concesionario.repository.ModeloRepository;
import com.example.concesionario.repository.VehiculoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoWebController {

    private final VehiculoRepository vehiculoRepository;
    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final ConcesionarioRepository concesionarioRepository;
    private final ExtraRepository extraRepository;

    public VehiculoWebController(VehiculoRepository vehiculoRepository,
                                 ModeloRepository modeloRepository,
                                 MarcaRepository marcaRepository,
                                 ConcesionarioRepository concesionarioRepository,
                                 ExtraRepository extraRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
        this.concesionarioRepository = concesionarioRepository;
        this.extraRepository = extraRepository;
    }

    // ── LISTA ────────────────────────────────────────────────────────────────

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("listaVehiculos", vehiculoRepository.findAll());
        return "vehiculos/lista";
    }

    // ── CREAR ────────────────────────────────────────────────────────────────

    @GetMapping({"/crear", "/nuevo"})
    public String crear(Model model) {

        Vehiculo vehiculo = new Vehiculo();


        Modelo modelo = new Modelo();
        modelo.setMarca(new Marca());
        vehiculo.setModelo(modelo);

        model.addAttribute("titulo", "Nuevo Vehículo");
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("accion", "/vehiculos/guardar");
        poblarSelectores(model);

        return "vehiculos/form";
}

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Vehiculo vehiculo,
                      @RequestParam(value = "modelo.id", required = false) Long modeloId,
                      @RequestParam(value = "concesionario.id", required = false) Long concId,
                      Model model) {

        System.out.println("=== DEBUG VEHICULO POST ===");
        System.out.println("modeloId from @RequestParam: " + modeloId);
        System.out.println("concesionarioId from @RequestParam: " + concId);
        System.out.println("codigoVehiculo: " + vehiculo.getCodigoVehiculo());
        System.out.println("===========================");

        if (modeloId != null) {
            modeloRepository.findById(modeloId).ifPresent(vehiculo::setModelo);
        }
        if (concId != null) {
            concesionarioRepository.findById(concId).ifPresent(vehiculo::setConcesionario);
        }

        try {
            vehiculoRepository.save(vehiculo);
            return "redirect:/vehiculos/lista";
        } catch (Exception ex) {
            model.addAttribute("titulo", "Nuevo Vehículo");
            model.addAttribute("vehiculo", vehiculo);
            model.addAttribute("accion", "/vehiculos/guardar");
            model.addAttribute("error", ex.getMessage());
            poblarSelectores(model);
            return "vehiculos/form";
        }
    }


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Vehiculo vehiculo = vehiculoRepository.findById(id).orElseThrow();


        if (vehiculo.getModelo() == null) {
            Modelo m = new Modelo();
            m.setMarca(new Marca());
            vehiculo.setModelo(m);
        } else if (vehiculo.getModelo().getMarca() == null) {
            vehiculo.getModelo().setMarca(new Marca());
        }

        model.addAttribute("titulo", "Editar Vehículo");
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("accion", "/vehiculos/editar/" + id);
        poblarSelectores(model);
        return "vehiculos/form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id,
                                 @ModelAttribute Vehiculo actualizado,
                                 Model model) {
        try {
            Vehiculo vehiculo = vehiculoRepository.findById(id).orElseThrow();

            vehiculo.setCodigoVehiculo(actualizado.getCodigoVehiculo());
            vehiculo.setVin(actualizado.getVin());
            vehiculo.setMatricula(actualizado.getMatricula());
            vehiculo.setFechaFabricacion(actualizado.getFechaFabricacion());
            vehiculo.setKilometraje(actualizado.getKilometraje());
            vehiculo.setColor(actualizado.getColor());
            vehiculo.setPrecioVenta(actualizado.getPrecioVenta());
            vehiculo.setEstado(actualizado.getEstado());
            vehiculo.setCombustible(actualizado.getCombustible());

            resolverRelacionesEnEdicion(actualizado, vehiculo);

            vehiculoRepository.save(vehiculo);
            return "redirect:/vehiculos/lista";

        } catch (Exception ex) {
            model.addAttribute("titulo", "Editar Vehículo");
            model.addAttribute("vehiculo", actualizado);
            model.addAttribute("accion", "/vehiculos/editar/" + id);
            model.addAttribute("error", ex.getMessage());
            poblarSelectores(model);
            return "vehiculos/form";
        }
    }



    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Vehiculo vehiculo = vehiculoRepository.findById(id).orElseThrow();
            Modelo modelo = vehiculo.getModelo();
            vehiculoRepository.delete(vehiculo);
            if (modelo != null) {
                modeloRepository.delete(modelo);
            }
            redirectAttributes.addFlashAttribute("success", "Vehículo eliminado correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar este vehículo.");
        }
        return "redirect:/vehiculos/lista";
    }



    private void poblarSelectores(Model model) {
        model.addAttribute("modelos", modeloRepository.findAll());
        model.addAttribute("concesionarios", concesionarioRepository.findAll());
        model.addAttribute("extras", extraRepository.findAll());
        model.addAttribute("marcas", marcaRepository.findAll());
    }

    private void resolverRelaciones(Vehiculo v) {


        if (v.getModelo() != null && v.getModelo().getId() != null) {
            Long modeloId = v.getModelo().getId();
            Modelo modelo = modeloRepository.findById(modeloId).orElseThrow();
            v.setModelo(modelo);
        }


        if (v.getConcesionario() != null && v.getConcesionario().getId() != null) {
            v.setConcesionario(concesionarioRepository.findById(v.getConcesionario().getId()).orElseThrow());
        }


        if (v.getExtras() != null && !v.getExtras().isEmpty()) {
            List<Extra> extras = v.getExtras().stream()
                    .map(e -> extraRepository.findById(e.getId()).orElseThrow())
                    .toList();
            v.setExtras(extras);
        }
    }

    private void resolverRelacionesEnEdicion(Vehiculo origen, Vehiculo destino) {


        if (origen.getModelo() != null && origen.getModelo().getId() != null) {
            Long modeloId = origen.getModelo().getId();
            Modelo modelo = modeloRepository.findById(modeloId).orElseThrow();
            destino.setModelo(modelo);
        }


        if (origen.getConcesionario() != null && origen.getConcesionario().getId() != null) {
            destino.setConcesionario(concesionarioRepository.findById(origen.getConcesionario().getId()).orElseThrow());
        }


        if (origen.getExtras() != null && !origen.getExtras().isEmpty()) {
            List<Extra> extras = origen.getExtras().stream()
                    .map(e -> extraRepository.findById(e.getId()).orElseThrow())
                    .toList();
            destino.setExtras(extras);
        }
    }
}
