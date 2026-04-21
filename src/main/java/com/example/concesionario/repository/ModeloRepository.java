package com.example.concesionario.repository;

import com.example.concesionario.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "modelos", collectionResourceRel = "modelos")
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    @RestResource(path = "por-nombre", rel = "por-nombre")
    List<Modelo> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    @RestResource(path = "por-marca", rel = "por-marca")
    List<Modelo> findByMarcaId(@Param("marcaId") Long marcaId);
}
