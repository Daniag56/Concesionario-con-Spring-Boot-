package com.example.concesionario.controller;

import com.example.concesionario.entity.CarritoItem;
import com.example.concesionario.entity.Usuario;
import com.example.concesionario.entity.Vehiculo;
import com.example.concesionario.repository.CarritoItemRepository;
import com.example.concesionario.repository.VehiculoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final VehiculoRepository vehiculoRepository;
    private final CarritoItemRepository carritoItemRepository;

    public ClienteController(VehiculoRepository vehiculoRepository, CarritoItemRepository carritoItemRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.carritoItemRepository = carritoItemRepository;
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("titulo", "Catálogo de Vehículos");
        model.addAttribute("vehiculos", vehiculoRepository.findAll());
        return "cliente/catalogo";
    }

    @GetMapping("/vehiculo/{id}")
    public String detalleVehiculo(@PathVariable Long id, Model model) {
        Vehiculo vehiculo = vehiculoRepository.findById(id).orElseThrow();
        model.addAttribute("titulo", "Detalle de Vehículo");
        model.addAttribute("vehiculo", vehiculo);
        return "cliente/detalle";
    }

    @PostMapping("/carrito/agregar/{vehiculoId}")
    public String agregarAlCarrito(@PathVariable Long vehiculoId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId).orElseThrow();

        boolean yaExiste = carritoItemRepository.findByUsuarioIdAndVehiculoId(usuario.getId(), vehiculoId).isPresent();
        if (yaExiste) {
            redirectAttributes.addFlashAttribute("error", "Ese vehículo ya está en MiCarrito.");
            return "redirect:/cliente/catalogo";
        }

        CarritoItem item = new CarritoItem();
        item.setUsuario(usuario);
        item.setVehiculo(vehiculo);
        carritoItemRepository.save(item);

        redirectAttributes.addFlashAttribute("success", "Vehículo añadido a MiCarrito.");
        return "redirect:/cliente/carrito";
    }

    @GetMapping("/carrito")
    public String verCarrito(Authentication authentication, Model model) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<CarritoItem> items = carritoItemRepository.findByUsuarioIdOrderByCreatedAtDesc(usuario.getId());
        double total = items.stream()
                .map(CarritoItem::getVehiculo)
                .mapToDouble(v -> v.getPrecioVenta() == null ? 0.0 : v.getPrecioVenta())
                .sum();

        model.addAttribute("titulo", "MiCarrito");
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "cliente/carrito";
    }

    @PostMapping("/carrito/eliminar/{itemId}")
    public String eliminarDelCarrito(@PathVariable Long itemId,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        carritoItemRepository.deleteByIdAndUsuarioId(itemId, usuario.getId());
        redirectAttributes.addFlashAttribute("success", "Elemento eliminado de MiCarrito.");
        return "redirect:/cliente/carrito";
    }
}
