
package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.HistoricoResponse;
import com.example.autofix.domain.entities.Historico;
import com.example.autofix.domain.entities.Usuario;
import com.example.autofix.domain.repository.HistoricoRepository;
import com.example.autofix.domain.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContextoSegurancaService contexto;

    public void registrar(String descricao) {

        Long usuarioId = contexto.getUsuarioLogadoId();

        if (usuarioId == null) {
            return;
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElse(null);

        if (usuario == null) {
            return;
        }

        Historico historico = new Historico();

        historico.setDescricao(descricao);
        historico.setUsuario(usuario);
        historico.setMecanica(usuario.getMecanica());

        historicoRepository.save(historico);
    }

    public List<HistoricoResponse> listarHistorico() {

        List<Historico> historicos;

        // Administrador global: visualiza o histórico de todas as mecânicas
        if (contexto.isAdmin()) {

            historicos = historicoRepository.findAll();

        }
        // Administrador da mecânica: visualiza todo o histórico da própria mecânica
        else if (contexto.isAdminMecanica()) {

            Long mecanicaId = contexto.getMecanicaId();

            if (mecanicaId == null) {
                return List.of();
            }

            historicos = historicoRepository
                    .findByMecanica_IdOrderByDataHoraDesc(mecanicaId);

        }
        // Usuário comum: visualiza somente o próprio histórico
        else {

            Long usuarioId = contexto.getUsuarioLogadoId();

            if (usuarioId == null) {
                return List.of();
            }

            historicos = historicoRepository
                    .findByUsuario_IdOrderByDataHoraDesc(usuarioId);
        }

        return historicos.stream()
                .sorted((a, b) -> b.getDataHora().compareTo(a.getDataHora()))
                .map(HistoricoResponse::new)
                .toList();
    }
}