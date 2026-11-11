package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.AtualizarStatusOrdemServicoRequest;
import com.example.autofix.application.DTOs.OrdemServicoRequest;
import com.example.autofix.application.DTOs.OrdemServicoResponse;
import com.example.autofix.application.services.OrdemServicoService;
import com.example.autofix.application.DTOs.EnumStatusOrdemServico;
import com.example.autofix.domain.repository.OrdemServicoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordemservicos")
@Tag(name = "Ordens de Serviço", description = "Grupo de APIs responsável pelo controle das ordens de serviço")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private OrdemServicoService ordemServicoService;

    @GetMapping
    @Operation(
            summary = "Método de consulta de ordens de serviço",
            description = "Método responsável por consultar todas as ordens de serviço cadastradas no sistema")
    public ResponseEntity<List<OrdemServicoResponse>> listarTodos() {

        return ResponseEntity.ok(ordemServicoService.listarTodasOrdensServicoTable());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta uma Ordem de Serviço por ID",
            description = "Método responsável por buscar uma Ordem de Serviço específico através do seu ID.")
    public ResponseEntity<OrdemServicoResponse> buscarPorId(@PathVariable Long id) {

        OrdemServicoResponse ordemServicoBanco = ordemServicoService.buscarPorId(id);

        if (ordemServicoBanco != null) {
            return ResponseEntity.ok(ordemServicoBanco);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
            summary = "Método de criação de ordem de serviço",
            description = "Método responsável por cadastrar uma nova ordem de serviço no sistema"
    )
    public ResponseEntity<?> criar(@RequestBody OrdemServicoRequest ordemServico) {

        try {
            OrdemServicoResponse ordemServicoBanco =
                    ordemServicoService.salvarOrdemServico(ordemServico, 0L);

            return ResponseEntity.status(HttpStatus.CREATED).body(ordemServicoBanco);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Método de atualização de status de ordem de serviço",
            description = "Método responsável por atualizar apenas o status de uma ordem de serviço cadastrada no sistema"
    )
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusOrdemServicoRequest statusRequest
    ) {

        try {
            var atualizarOrdemServico = ordemServicoService.atualizarStatus(
                    statusRequest.statusOrdemServico(), id
            );

            if (atualizarOrdemServico) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    @Operation(
            summary = "Método de atualização de ordem de serviço",
            description = "Método responsável por atualizar todos os dados de uma ordem de serviço cadastrada no sistema")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody OrdemServicoRequest ordemServico
    ) {

        try {
            OrdemServicoResponse ordemServicoBanco =
                    ordemServicoService.salvarOrdemServico(ordemServico, id);

            if (ordemServicoBanco != null) {
                return ResponseEntity.ok(ordemServicoBanco);
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(
            summary = "Método de exclusão de ordem de serviço",
            description = "Método responsável por excluir (cancelar) uma ordem de serviço cadastrada no sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        var atualizarOrdemServico = ordemServicoService.atualizarStatus(
                EnumStatusOrdemServico.EXCLUIDO, id
        );

        if (atualizarOrdemServico) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

}