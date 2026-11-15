
package com.example.autofix.application.DTOs;

import com.example.autofix.domain.entities.Historico;

import java.time.LocalDateTime;

public record HistoricoResponse(
        Long id,
        String descricao,
        LocalDateTime dataHora,
        String usuario,
        String mecanica
) {

    public HistoricoResponse(Historico historico) {
        this(
                historico.getId(),
                historico.getDescricao(),
                historico.getDataHora(),
                historico.getUsuario() != null
                        ? historico.getUsuario().getNome()
                        : "Sistema",
                historico.getMecanica() != null
                        ? historico.getMecanica().getNomeFantasia()
                        : "Administrador geral"
        );
    }
}
