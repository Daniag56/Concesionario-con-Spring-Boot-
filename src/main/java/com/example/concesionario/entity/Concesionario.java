package com.example.concesionario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "concesionario")
public class Concesionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nombreComercial;

    @NotBlank
    @Size(max = 100)
    private String ciudad;

    @NotBlank
    @Size(max = 200)
    private String direccion;

    private Integer plazasExposicion;
    private Integer metrosCuadrados;

    @OneToMany(mappedBy = "concesionario", fetch = FetchType.LAZY)
    private List<Vehiculo> vehiculos;

    public Concesionario() {}

    public Long getId() {
        return id;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getPlazasExposicion() {
        return plazasExposicion;
    }

    public void setPlazasExposicion(Integer plazasExposicion) {
        this.plazasExposicion = plazasExposicion;
    }

    public Integer getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(Integer metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
}
