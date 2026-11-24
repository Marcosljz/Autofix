package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.MecanicaCnpjResponse;
import com.example.autofix.application.services.MecanicaCnpjService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/mecanicas")
@RequiredArgsConstructor
@Tag(name = "Mecânicas",
        description = "APIs responsáveis pelas consultas de dados das mecânicas")
public class MecanicaCnpjController {

    private final MecanicaCnpjService mecanicaCnpjService;

    @GetMapping("/cnpj/{cnpj}")
    @Operation(
            summary = "Consultar mecânica pelo CNPJ",
            description = "Busca os dados de uma mecânica através do CNPJ informado"
    )
    public ResponseEntity<MecanicaCnpjResponse> buscarMecanicaPorCnpj(
            @PathVariable String cnpj) {

        return ResponseEntity.ok(
                mecanicaCnpjService.buscarMecanicaPorCnpj(cnpj)
        );
    }
}

