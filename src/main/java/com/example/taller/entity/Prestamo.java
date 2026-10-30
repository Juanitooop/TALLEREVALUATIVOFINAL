/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name = "prestamos")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //Estos dos Long son los que se escriben: se guardan en libro_id y usuario_id
    @Column(name = "libro_id")
    private Long libroId;
    
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    private LocalDate fechaPrestamo;
    
    private LocalDate fechaDevolucion;
    
    private Boolean devuelto;
    

    @ManyToOne
    @JoinColumn(name = "libro_id", insertable = false, updatable = false)
    private Libro libro;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;

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

    public Libro getLibro() {
        return libro;
    }
    
    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
