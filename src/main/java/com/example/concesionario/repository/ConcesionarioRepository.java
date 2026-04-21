package com.example.concesionario.repository;

import com.example.concesionario.entity.Concesionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "concesionarios", collectionResourceRel = "concesionarios")
public interface ConcesionarioRepository extends JpaRepository<Concesionario, Long> {

    @RestResource(path = "por-ciudad", rel = "por-ciudad")
    List<Concesionario> findByCiudadIgnoreCase(@Param("ciudad") String ciudad);
}

