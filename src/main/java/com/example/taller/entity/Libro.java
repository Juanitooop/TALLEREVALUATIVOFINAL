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


@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    
    @Column(unique = true)
    private String isbn;
    
    private Integer anio;
    
    @Column(name = "autor_id")
    private Long autorId;
    

    @ManyToOne
    @JoinColumn(name = "autor_id", insertable = false, updatable = false)
    private Autor autor;

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnio() {
        return anio;
    }
    
    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getAutorId() {
        return autorId;
    }
    
    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public Autor getAutor() {
        return autor;
    }
    
    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
