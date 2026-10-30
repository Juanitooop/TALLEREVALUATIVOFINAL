/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.dto;

import com.example.taller.entity.Prestamo;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PrestamoDto {
    private Long id;
    
    //Lo que manda el cliente: solo los ids
    @NotNull(message = "El libro es obligatorio")
    private Long libroId;
    
    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;
    
    @NotNull(message = "La fecha de prestamo es obligatoria")
    private LocalDate fechaPrestamo;
    
    private LocalDate fechaDevolucion;
    
    private Boolean devuelto;
    
    //Lo que devuelve la API: el libro y el usuario completos, para mostrar el detalle
    private LibroDto libro;
    
    private UsuarioDto usuario;

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Long getLibroId() {
        return libroId;
    }
    
    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
    
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
    
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Boolean getDevuelto() {
        return devuelto;
    }
    
    public void setDevuelto(Boolean devuelto) {
        this.devuelto = devuelto;
    }

    public LibroDto getLibro() {
        return libro;
    }
    
    public void setLibro(LibroDto libro) {
        this.libro = libro;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }
    
    public void setUsuario(UsuarioDto usuario) {
        this.usuario = usuario;
    }

    //Convierte lo que llega por la API en una entity, para poder guardarla
    public Prestamo toEntity() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(this.id);
        prestamo.setLibroId(this.libroId);
        prestamo.setUsuarioId(this.usuarioId);
        prestamo.setFechaPrestamo(this.fechaPrestamo);
        prestamo.setFechaDevolucion(this.fechaDevolucion);
        prestamo.setDevuelto(this.devuelto);
        return prestamo;
    }
    
    //Convierte la entity que viene de la base en lo que se devuelve por la API
    public static PrestamoDto fromEntity(Prestamo prestamo) {
        PrestamoDto dto = new PrestamoDto();
        dto.setId(prestamo.getId());
        dto.setLibroId(prestamo.getLibroId());
        dto.setUsuarioId(prestamo.getUsuarioId());
        dto.setFechaPrestamo(prestamo.getFechaPrestamo());
        dto.setFechaDevolucion(prestamo.getFechaDevolucion());
        dto.setDevuelto(prestamo.getDevuelto());
        if (prestamo.getLibro() != null) {
            dto.setLibro(LibroDto.fromEntity(prestamo.getLibro()));
        }
        if (prestamo.getUsuario() != null) {
            dto.setUsuario(UsuarioDto.fromEntity(prestamo.getUsuario()));
        }
        return dto;
    }
}
