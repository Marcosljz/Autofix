package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.AtualizarStatusPecaRequest;
import com.example.autofix.application.DTOs.PecaRequest;
import com.example.autofix.application.DTOs.PecaResponse;
import com.example.autofix.application.services.PecaService;
import com.example.autofix.application.DTOs.EnumStatusPeca;

import com.example.autofix.domain.repository.PecaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pecas")
@Tag(name = "Peças",
        description = "Grupo de APIs responsável pelo controle e gerenciamento de peças")
public class PecaController {

    @Autowired
    private PecaRepository pecaRepository;


    @Autowired
    private PecaService pecaService;

    @GetMapping
    @Operation(
            summary = "Método de consulta de peças",
            description = "Método responsável por consultar todas as peças cadastradas no sistema")
    public ResponseEntity<List<PecaResponse>> listarTodos() {

        return ResponseEntity.ok(pecaService.listarTodasPecasTable());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta de peça por ID",
            description = "Método responsável por buscar uma peça específica através do seu ID.")
    public ResponseEntity<PecaResponse> buscarPorid(@PathVariable Long id) {

        PecaResponse pecaBanco = pecaService.buscarPorId(id);

        if (pecaBanco != null) {
            return ResponseEntity.ok(pecaBanco);
        }

        return ResponseEntity.notFound().build();
    }


    @PostMapping
    @Operation(
            summary = "Método de criação de peça",
            description = "Método responsável por cadastrar uma nova peça no sistema")
    public ResponseEntity<?> criar(@RequestBody PecaRequest peca) {

        try {
            PecaResponse pecaBanco = pecaService.salvarPeca(peca, 0L);
            return ResponseEntity.status(HttpStatus.CREATED).body(pecaBanco);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Método de atualização de status de peça",
            description = "Método responsável por atualizar apenas o status de uma peça cadastrada no sistema")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusPecaRequest statusRequest
    ) {

        var atualizarPeca = pecaService.atualizarStatus(statusRequest.statusPeca(), id);

        if (atualizarPeca) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Método de atualização de peça",
            description = "Método responsável por atualizar todos os dados de uma peça cadastrada no sistema")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody PecaRequest peca
    ) {

        try {
            PecaResponse pecaBanco = pecaService.salvarPeca(peca, id);

            if (pecaBanco != null) {
                return ResponseEntity.ok(pecaBanco);
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(
            summary = "Método de exclusão de peça",
            description = "Método responsável por alterar o status da peça para EXCLUIDO")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        var atualizarPeca = pecaService.atualizarStatus(EnumStatusPeca.EXCLUIDO, id);

        if (atualizarPeca) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}