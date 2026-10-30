package com.example.taller.controller;

import com.example.taller.entity.Prestamo;
import com.example.taller.entity.Rol;
import com.example.taller.entity.Usuario;
import com.example.taller.repository.UsuarioRepository;
import com.example.taller.service.PrestamoService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PrestamoControllerConsultaTest {
    private PrestamoService servicio;
    private UsuarioRepository usuarios;
    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        servicio = mock(PrestamoService.class);
        usuarios = mock(UsuarioRepository.class);
        PrestamoController controller = new PrestamoController();
        controller.prestamoService = servicio;
        controller.usuarioRepository = usuarios;

        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        usuario("admin@test.local", 1L, Rol.ADMIN);
        usuario("lectorA@test.local", 2L, Rol.LECTOR);
        usuario("lectorB@test.local", 3L, Rol.LECTOR);
        prestamo(10L, 2L);
        prestamo(20L, 3L);
        when(servicio.getPrestamo(999L)).thenReturn(Optional.empty());
    }

    // Identidad simulada solo en el test: no conecta con la BD ni firma tokens.
    private JwtAuthenticationToken identidad(String email) {
        Jwt token = Jwt.withTokenValue("token-de-prueba")
                .header("alg", "HS256").subject(email).build();
        return new JwtAuthenticationToken(token);
    }
    private void usuario(String email, Long id, Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(rol);
        when(usuarios.findByEmailIgnoreCase(email)).thenReturn(Optional.of(usuario));
    }

    private void prestamo(Long id, Long propietario) {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(id);
        prestamo.setUsuarioId(propietario);
        when(servicio.getPrestamo(id)).thenReturn(Optional.of(prestamo));
    }

    @Test
    void administradorConsultaPrestamosDeAmbosLectores() throws Exception {
        mvc.perform(get("/prestamos/10").principal(identidad("admin@test.local")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.usuarioId").value(2));
        mvc.perform(get("/prestamos/20").principal(identidad("admin@test.local")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.usuarioId").value(3));
        verify(servicio, never()).saveOrUpdate(any());
        verify(servicio, never()).delete(any());
    }

    @Test
    void lectorAConsultaElSuyo() throws Exception {
        mvc.perform(get("/prestamos/10").principal(identidad("lectorA@test.local")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void lectorBConsultaElSuyo() throws Exception {
        mvc.perform(get("/prestamos/20").principal(identidad("lectorB@test.local")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(20));
    }

    @Test
    void lectorANoConsultaElDeB() throws Exception {
        mvc.perform(get("/prestamos/20").principal(identidad("lectorA@test.local")))
                .andExpect(status().isForbidden());
    }

    @Test
    void lectorBNoConsultaElDeA() throws Exception {
        mvc.perform(get("/prestamos/10").principal(identidad("lectorB@test.local")))
                .andExpect(status().isForbidden());
    }

    @Test
    void inexistenteDevuelve404ParaLosTresUsuarios() throws Exception {
        for (String email : new String[]{"admin@test.local", "lectorA@test.local", "lectorB@test.local"}) {
            mvc.perform(get("/prestamos/999").principal(identidad(email)))
                    .andExpect(status().isNotFound());
        }
        verifyNoInteractions(usuarios);
    }
}

