package com.example.autofix.controllers;

import com.example.autofix.DTOs.AtualizarStatusOrdemServicoRequest;
import com.example.autofix.entities.EnumStatusOrdemServico;
import com.example.autofix.entities.OrdemServico;
import com.example.autofix.entities.Usuario;
import com.example.autofix.repository.OrdemServicoRepository;
import com.example.autofix.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordemservicos")
@Tag(name = "Ordens de Serviço", description = "Grupo de APIs responsável pelo controle das ordens de serviço")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @GetMapping
    @Operation(
            summary = "Método de consulta de ordens de serviço",
            description = "Método responsável por consultar todas as ordens de serviço cadastradas no sistema")
    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(ordemServicoRepository.findAll());
    }

    @PostMapping
    @Operation(
            summary = "Método de criação de ordem de serviço",
            description = "Método responsável por cadastrar uma nova ordem de serviço no sistema"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrdemServico> criar(@RequestBody OrdemServico ordemServico){

        var ordemServicoBanco =  ordemServicoRepository.save(ordemServico);
        return ResponseEntity.ok(ordemServicoBanco);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Método de atualização de status de ordem de serviço",
            description = "Método responsável por atualizar apenas o status de uma ordem de serviço cadastrada no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusOrdemServicoRequest statusRequest){

        OrdemServico ordemServicoBanco = ordemServicoRepository.findById(id).orElse(null);
        if(ordemServicoBanco!= null ){
            ordemServicoBanco.setStatusOrdemServico(statusRequest.statusOrdemServico());
            ordemServicoRepository.save(ordemServicoBanco);
            return  ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Método de atualização de ordem de serviço",
            description = "Método responsável por atualizar todos os dados de uma ordem de serviço cadastrada no sistema")
    public ResponseEntity<OrdemServico> atualizar(@PathVariable Long id, @RequestBody OrdemServico ordemServico){

        try{
            OrdemServico ordemServicoBanco = ordemServicoRepository.findById(id).orElse(null);
            if(ordemServicoBanco!= null ){
                ordemServicoBanco.setDataAbertura(ordemServico.getDataAbertura());
                ordemServicoBanco.setDataConclusao(ordemServico.getDataConclusao());
                ordemServicoBanco.setStatusOrdemServico(ordemServico.getStatusOrdemServico());
                ordemServicoBanco.setDescricao(ordemServico.getDescricao());
                ordemServicoRepository.save(ordemServicoBanco);
                return  ResponseEntity.ok().build();
            }
            return  ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping("/{id}/excluir")
    @Operation(
            summary = "Método de exclusão de ordem de serviço",
            description = "Método responsável por excluir (cancelar) uma ordem de serviço cadastrada no sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        OrdemServico ordemServicoBanco = ordemServicoRepository.findById(id).orElse(null);
        if(ordemServicoBanco!= null ){
            ordemServicoBanco.setStatusOrdemServico(EnumStatusOrdemServico.CANCELADO);
            ordemServicoRepository.save(ordemServicoBanco);
            return  ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

}