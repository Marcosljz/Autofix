package com.example.autofix.application.DTOs;

import com.example.autofix.domain.entities.Usuario;
import com.example.autofix.domain.valueobject.CPF;

public record UsuarioLogado(Long id, String email, CPF cpf,
                            Long mecanica_id,
                            java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities) {

    public UsuarioLogado(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getMecanica()==null ?
                        null
                        :usuario.getMecanica().getId(),
                usuario.getAuthorities()
        );
    }
}

