package com.example.taller.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.taller.entity.Usuario;

@Service
public class JwtService {
    private final JwtEncoder encoder;
    private final Duration vigencia;

    //"${app.jwt.vigencia}" --> eso es nuevo. Con eso lo trae del .properties
    public JwtService(JwtEncoder encoder, @Value("${app.jwt.vigencia}") Long minutos) {
        this.encoder = encoder;
        this.vigencia = Duration.ofMinutes(minutos);
    }

    public String generar(Usuario usuario) {
        Instant ahora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(usuario.getEmail())
                .claim("rol", List.of(usuario.getRol().name()))
                .issuedAt(ahora)
                .expiresAt(ahora.plus(vigencia))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}