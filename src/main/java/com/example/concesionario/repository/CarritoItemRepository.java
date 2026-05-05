package com.example.concesionario.repository;

import com.example.concesionario.entity.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    Optional<CarritoItem> findByUsuarioIdAndVehiculoId(Long usuarioId, Long vehiculoId);

    Optional<CarritoItem> findByIdAndUsuarioId(Long id, Long usuarioId);
}
