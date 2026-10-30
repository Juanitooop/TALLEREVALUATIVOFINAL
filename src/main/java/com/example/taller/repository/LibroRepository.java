/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.taller.repository;

import com.example.taller.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LibroRepository extends JpaRepository<Libro, Long>{
    
    //tocó hacer esta consulta para la paginacion que la profe pidió en los requisitos. Puro sql
    @Query("select l from Libro l where lower(l.titulo) like lower(concat('%', :texto, '%')) "
            + "or lower(l.autor.nombre) like lower(concat('%', :texto, '%')) "
            + "or lower(l.autor.apellido) like lower(concat('%', :texto, '%'))")
    Page<Libro> buscar(@Param("texto") String texto, Pageable pageable);
}
