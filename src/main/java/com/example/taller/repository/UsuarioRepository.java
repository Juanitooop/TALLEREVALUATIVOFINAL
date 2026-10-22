/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.taller.repository;

import com.example.taller.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author alber
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    //Aqui igual que en libroRepository pero pa los usuarios
    @Query("select u from Usuario u where lower(u.nombre) like lower(concat('%', :texto, '%')) "
            + "or lower(u.username) like lower(concat('%', :texto, '%'))")
    Page<Usuario> buscar(@Param("texto") String texto, Pageable pageable);
}
