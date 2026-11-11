package com.example.autofix.application.DTOs;

import com.example.autofix.domain.entities.OrdemServico;

import java.time.LocalDateTime;

public record OrdemServicoResponse(Long id, LocalDateTime dataAbertura, LocalDateTime dataConclusao, EnumStatusOrdemServico statusOrdemServico, String descricao,Long veiculoId) {
    public OrdemServicoResponse(OrdemServico ordemServicoEntidade) {
        this(
                ordemServicoEntidade.getId(),
                ordemServicoEntidade.getDataAbertura(),
                ordemServicoEntidade.getDataConclusao(),
                ordemServicoEntidade.getStatusOrdemServico(),
                ordemServicoEntidade.getDescricao(),
                ordemServicoEntidade.getVeiculo() != null
                        ? ordemServicoEntidade.getVeiculo().getId()
                        : null

        );
    }
}
