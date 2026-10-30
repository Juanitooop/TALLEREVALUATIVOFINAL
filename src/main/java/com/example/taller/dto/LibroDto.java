   /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.dto;

import com.example.taller.entity.Libro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class LibroDto {
    private Long id;
    
    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;
    
    private String isbn;
    
    private Integer anio;
    
    //Lo que manda el cliente: solo el id del autor
    @NotNull(message = "El autor es obligatorio")
    private Long autorId;
    
    //Lo que devuelve la API: el autor completo, para mostrar el detalle
    private AutorDto autor;

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

    public AutorDto getAutor() {
        return autor;
    }
    
    public void setAutor(AutorDto autor) {
        this.autor = autor;
    }

    //Convierte lo que llega por la API en una entity, para poder guardarla
    public Libro toEntity() {
        Libro libro = new Libro();
        libro.setId(this.id);
        libro.setTitulo(this.titulo);
        libro.setIsbn(this.isbn);
        libro.setAnio(this.anio);
        libro.setAutorId(this.autorId);
        return libro;
    }
    
    //Convierte la entity que viene de la base en lo que se devuelve por la API
    public static LibroDto fromEntity(Libro libro) {
        LibroDto dto = new LibroDto();
        dto.setId(libro.getId());
        dto.setTitulo(libro.getTitulo());
        dto.setIsbn(libro.getIsbn());
        dto.setAnio(libro.getAnio());
        dto.setAutorId(libro.getAutorId());
        if (libro.getAutor() != null) {
            dto.setAutor(AutorDto.fromEntity(libro.getAutor()));
        }
        return dto;
    }
}
