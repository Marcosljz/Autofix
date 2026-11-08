package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.UsuarioLogado;
import com.example.autofix.domain.entities.Mecanica;
import com.example.autofix.domain.repository.MecanicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ContextoSegurancaService {

    @Autowired
    private MecanicaRepository mecanicaRepository;

    public Object getLogado() {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();

        return autenticacao == null ? null : autenticacao.getPrincipal();
    }

    public boolean isAdmin() {
        return getLogado() instanceof UsuarioLogado usuario
                && usuario.authorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isAdminMecanica() {
        return getLogado() instanceof UsuarioLogado usuario
                && usuario.authorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MECANICA"));
    }

    public Long getUsuarioLogadoId() {
        Object logado = getLogado();

        if (logado instanceof UsuarioLogado usuario) {
            return usuario.id();
        }

        return null;
    }

    public Long getMecanicaId() {
        Object logado = getLogado();

        if (logado instanceof UsuarioLogado usuario) {
            return usuario.mecanica_id();
        }

        return null;
    }

    public Mecanica getMecanicaDoLogado() {
        Long mecanicaId = getMecanicaId();

        return mecanicaId == null
                ? null
                : mecanicaRepository.getReferenceById(mecanicaId);
    }

    public boolean podeAcessar(Mecanica mecanica) {
        if (isAdmin()) {
            return true;
        }

        Long mecanicaId = getMecanicaId();

        return mecanica != null
                && mecanicaId != null
                && mecanica.getId().equals(mecanicaId);
    }
}