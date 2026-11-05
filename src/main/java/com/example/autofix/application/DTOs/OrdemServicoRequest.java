package com.example.autofix.application.DTOs;

import java.time.LocalDateTime;

public record OrdemServicoRequest(LocalDateTime dataAbertura, LocalDateTime dataConclusao, String descricao) {
}
