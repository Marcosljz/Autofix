
package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.OrdemServicoPecaRequest;
import com.example.autofix.application.DTOs.PecaOrdemServicoResponse;
import com.example.autofix.application.services.OrdemServicoPecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens-servico-pecas")
public class OrdemServicoPecaController {

    @Autowired
    private OrdemServicoPecaService service;

    @PostMapping
    public ResponseEntity<PecaOrdemServicoResponse> salvar(
            @RequestBody OrdemServicoPecaRequest request) {

        PecaOrdemServicoResponse resposta = service.salvar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/{ordemServicoId}")
    public ResponseEntity<List<PecaOrdemServicoResponse>> listarPorOrdemServico(
            @PathVariable Long ordemServicoId) {

        return ResponseEntity.ok(
                service.listarPorOrdemServico(ordemServicoId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PecaOrdemServicoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody OrdemServicoPecaRequest request) {

        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
