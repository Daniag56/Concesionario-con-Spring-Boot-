package com.example.concesionario.repository;

import com.example.concesionario.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(path = "marcas", collectionResourceRel = "marcas")
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    @RestResource(path = "por-nombre", rel = "por-nombre")
    Optional<Marca> findByNombre(@Param("nombre") String nombre);
}
