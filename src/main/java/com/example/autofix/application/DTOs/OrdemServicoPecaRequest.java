package com.example.autofix.application.DTOs;

public record OrdemServicoPecaRequest(
        Long ordemServicoId,
        Long pecaId,
        Integer quantidade
) {
}