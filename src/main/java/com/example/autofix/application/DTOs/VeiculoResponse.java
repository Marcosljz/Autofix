package com.example.autofix.application.DTOs;

import com.example.autofix.domain.entities.EnumStatusVeiculo;
import com.example.autofix.domain.entities.Veiculo;

public record VeiculoResponse(Long id , String placa, String Modelo, String ano , String cor, Double quilometragem, EnumStatusVeiculo statusVeiculo) {

    public VeiculoResponse(Veiculo veiculoEntidade) {
        this(
                veiculoEntidade.getId(),
                veiculoEntidade.getPlaca(),
                veiculoEntidade.getModelo(),
                veiculoEntidade.getAno(),
                veiculoEntidade.getCor(),
                veiculoEntidade.getQuilometragem(),
                veiculoEntidade.getStatusVeiculo()
        );
    }
}
