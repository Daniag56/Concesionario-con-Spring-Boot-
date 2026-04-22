package com.example.concesionario.repository;

import com.example.concesionario.entity.EstadoVehiculo;
import com.example.concesionario.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "vehiculos", collectionResourceRel = "vehiculos")
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    @RestResource(path = "por-vin", rel = "por-vin")
    Optional<Vehiculo> findByVin(@Param("vin") String vin);


    @RestResource(path = "por-estado", rel = "por-estado")
    List<Vehiculo> findByEstado(@Param("estado") EstadoVehiculo estado);

    @RestResource(path = "por-modelo", rel = "por-modelo")
    List<Vehiculo> findByModeloId(@Param("modeloId") Long modeloId);

    @RestResource(path = "por-concesionario", rel = "por-concesionario")
    List<Vehiculo> findByConcesionarioId(@Param("concesionarioId") Long concesionarioId);
}


