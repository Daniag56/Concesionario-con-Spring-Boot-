package com.example.concesionario.repository;

import com.example.concesionario.entity.Extra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "extras", collectionResourceRel = "extras")
public interface ExtraRepository extends JpaRepository<Extra, Long> {

    @RestResource(path = "por-nombre", rel = "por-nombre")
    List<Extra> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
}
