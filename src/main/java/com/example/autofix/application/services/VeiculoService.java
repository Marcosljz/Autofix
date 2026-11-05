package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.VeiculoRequest;
import com.example.autofix.application.DTOs.VeiculoResponse;
import com.example.autofix.domain.entities.EnumStatusVeiculo;
import com.example.autofix.domain.entities.Veiculo;
import com.example.autofix.domain.repository.VeiculoRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepository;
    public @Nullable List<VeiculoResponse> ListarTodosVeiculosTable() {
        return veiculoRepository.findAll()
                .stream()
                .map(VeiculoResponse::new)
                .toList();
    }

    public VeiculoResponse buscarPorId(Long id) {

        var veiculo = veiculoRepository.findById(id).orElse(null);
        if (veiculo!=null){
            return null;
        }
        return new VeiculoResponse(veiculo);
    }

    public VeiculoResponse salvarVeiculo(VeiculoRequest veiculo, long id){

    }

    public boolean atualizarStatusVeiculo(EnumStatusVeiculo statusVeiculo, Long id) {
        Veiculo veiculoBanco = veiculoRepository.findById(id).orElse(null);
        if(veiculoBanco!=null){
            veiculoBanco.setStatusVeiculo(statusVeiculo);
            veiculoRepository.save(veiculoBanco);
            return true;
        }
        return false;
    }
}
