
package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.MecanicaCnpjResponse;
import com.example.autofix.domain.interfaces.IMecanicaExternalRepository;
import com.example.autofix.infra.dto.CnpjResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MecanicaCnpjService {

    private final IMecanicaExternalRepository mecanicaExternalRepository;

    public MecanicaCnpjResponse buscarMecanicaPorCnpj(String cnpj) {

        CnpjResponse resposta = mecanicaExternalRepository.buscarMecanicaPorCnpj(cnpj);

        return new MecanicaCnpjResponse(
                resposta.getRazaoSocial(),
                resposta.getCnpj(),
                resposta.getNomeFantasia()
        );
    }
}
