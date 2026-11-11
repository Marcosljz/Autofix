package com.example.autofix.domain.interfaces;

import com.example.autofix.infra.dto.CnpjResponse;

public interface IMecanicaExternalRepository {

    CnpjResponse buscarMecanicaPorCnpj(String cnpj);

}