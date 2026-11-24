
package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.HistoricoResponse;
import com.example.autofix.application.services.HistoricoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/historico")
@Tag(name = "Histórico",
        description = "APIs responsáveis pela consulta do histórico")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    @Operation(
            summary = "Listar histórico",
            description = "Consulta os registros de histórico disponíveis para o usuário logado"
    )
    public ResponseEntity<List<HistoricoResponse>> listar() {

        return ResponseEntity.ok(
                historicoService.listarHistorico()
        );
    }
}
