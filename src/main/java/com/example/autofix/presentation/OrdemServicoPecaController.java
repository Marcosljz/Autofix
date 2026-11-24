
package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.OrdemServicoPecaRequest;
import com.example.autofix.application.DTOs.PecaOrdemServicoResponse;
import com.example.autofix.application.services.OrdemServicoPecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/ordens-servico-pecas")
@Tag(name = "Peças da Ordem de Serviço",
        description = "APIs responsáveis pelo gerenciamento das peças vinculadas às ordens de serviço")
public class OrdemServicoPecaController {

    @Autowired
    private OrdemServicoPecaService service;

    @PostMapping
    @Operation(
            summary = "Adicionar peça à ordem de serviço",
            description = "Vincula uma peça a uma ordem de serviço"
    )
    public ResponseEntity<PecaOrdemServicoResponse> salvar(
            @RequestBody OrdemServicoPecaRequest request) {

        PecaOrdemServicoResponse resposta = service.salvar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/{ordemServicoId}")
    @Operation(
            summary = "Listar peças da ordem de serviço",
            description = "Consulta as peças vinculadas a uma ordem de serviço pelo seu ID"
    )
    public ResponseEntity<List<PecaOrdemServicoResponse>> listarPorOrdemServico(
            @PathVariable Long ordemServicoId) {

        return ResponseEntity.ok(
                service.listarPorOrdemServico(ordemServicoId)
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar peça da ordem de serviço",
            description = "Atualiza os dados de uma peça vinculada à ordem de serviço"
    )
    public ResponseEntity<PecaOrdemServicoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody OrdemServicoPecaRequest request) {

        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir peça da ordem de serviço",
            description = "Remove o vínculo de uma peça com a ordem de serviço"
    )
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
