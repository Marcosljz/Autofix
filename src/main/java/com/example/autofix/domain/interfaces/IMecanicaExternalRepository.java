package com.example.autofix.domain.interfaces;

import com.example.autofix.domain.valueobject.DadosMecanicaCnpj;

public interface IMecanicaExternalRepository {
    DadosMecanicaCnpj buscarMecanicaPorCnpj(String cnpj);
}