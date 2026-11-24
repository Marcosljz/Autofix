package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.MecanicaCnpjResponse;
import com.example.autofix.domain.interfaces.IMecanicaExternalRepository;
import com.example.autofix.domain.valueobject.DadosMecanicaCnpj;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MecanicaCnpjService {

    private final IMecanicaExternalRepository mecanicaExternalRepository;

    public MecanicaCnpjResponse buscarMecanicaPorCnpj(String cnpj) {

        DadosMecanicaCnpj resposta =
                mecanicaExternalRepository.buscarMecanicaPorCnpj(cnpj);

        return new MecanicaCnpjResponse(
                resposta.razaoSocial(),
                resposta.cnpj(),
                resposta.nomeFantasia()
        );
    }
}