/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.service;

import com.example.taller.entity.Usuario;
import com.example.taller.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    //obtener todos, paginado
    public Page<Usuario> getUsuarios(int page, int size){
        return usuarioRepository.findAll(PageRequest.of(page, size));
    }
    
    //buscar por nombre o username, paginado
    public Page<Usuario> buscar(String texto, int page, int size){
        return usuarioRepository.buscar(texto, PageRequest.of(page, size));
    }
    
    //obtener uno
    public Optional<Usuario> getUsuario(Long id){
        return usuarioRepository.findById(id);
    }
    
    //Guardar: lo que llega es la contrasena en texto plano (viene del DTO), se hashea antes de la base
    public void saveOrUpdate(Usuario usuario){
        String hash = passwordEncoder.encode(usuario.getPasswordHash());
        usuario.setPasswordHash(hash);
        usuarioRepository.save(usuario);
    }
    
    public void delete(Long id){
        usuarioRepository.deleteById(id);
    }
}
