package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.MecanicaCnpjResponse;
import com.example.autofix.application.services.MecanicaCnpjService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mecanicas")
@RequiredArgsConstructor
public class MecanicaCnpjController {

    private final MecanicaCnpjService mecanicaCnpjService;

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<MecanicaCnpjResponse> buscarMecanicaPorCnpj(
            @PathVariable String cnpj) {

        return ResponseEntity.ok(
                mecanicaCnpjService.buscarMecanicaPorCnpj(cnpj)
        );
    }
}

