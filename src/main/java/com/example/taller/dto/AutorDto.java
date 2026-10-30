/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.dto;

import com.example.taller.entity.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;


public class AutorDto {
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    
    private String nacionalidad;
    
    @Past(message = "La fecha de nacimiento tiene que ser anterior a hoy")
    private LocalDate fechaNacimiento;

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }
    
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    //Convierte lo que llega por la API en una entity, para poder guardarla
    public Autor toEntity() {
        Autor autor = new Autor();
        autor.setId(this.id);
        autor.setNombre(this.nombre);
        autor.setApellido(this.apellido);
        autor.setNacionalidad(this.nacionalidad);
        autor.setFechaNacimiento(this.fechaNacimiento);
        return autor;
    }
    
    //Convierte la entity que viene de la base en lo que se devuelve por la API
    public static AutorDto fromEntity(Autor autor) {
        AutorDto dto = new AutorDto();
        dto.setId(autor.getId());
        dto.setNombre(autor.getNombre());
        dto.setApellido(autor.getApellido());
        dto.setNacionalidad(autor.getNacionalidad());
        dto.setFechaNacimiento(autor.getFechaNacimiento());
        return dto;
    }
}
