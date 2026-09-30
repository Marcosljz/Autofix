package com.example.autofix.controllers;

import com.example.autofix.DTOs.AtualizarStatusPecaRequest;
import com.example.autofix.entities.EnumStatusPeca;

import com.example.autofix.entities.Peca;

import com.example.autofix.repository.PecaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pecas")
@Tag(name = "Peças",
        description = "Grupo de APIs responsável pelo controle e gerenciamento de peças")
public class PecaController {

    @Autowired
    private PecaRepository pecaRepository;

    @GetMapping
    @Operation(
            summary = "Método de consulta de peças",
            description = "Método responsável por consultar todas as peças cadastradas no sistema")
    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(pecaRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta de peça por ID",
            description = "Método responsável por buscar uma peça específica através do seu ID.")
    public ResponseEntity<Peca> buscarPorId(@PathVariable Long id){

        Peca pecabanco = pecaRepository.findById(id).orElse(null);
        if(pecabanco!= null ) {

            return ResponseEntity.ok(pecabanco);
        }

        return  ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
            summary = "Método de criação de peça",
            description = "Método responsável por cadastrar uma nova peça no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Peca> criar(@RequestBody Peca peca){

        var pecaBanco =  pecaRepository.save(peca);
        return ResponseEntity.ok(pecaBanco);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Método de atualização de status de peça",
            description = "Método responsável por atualizar apenas o status de uma peça cadastrada no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusPecaRequest statusRequest){

        Peca pecaBanco = pecaRepository.findById(id).orElse(null);
        if(pecaBanco!= null ){
            pecaBanco.setStatuspeca(statusRequest.statusPeca());
            pecaRepository.save(pecaBanco);
            return  ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Método de atualização de peça",
            description = "Método responsável por atualizar todos os dados de uma peça cadastrada no sistema")
    public ResponseEntity<Peca> atualizar(@PathVariable Long id, @RequestBody Peca peca){

        try{
            Peca pecaBanco = pecaRepository.findById(id).orElse(null);
            if(pecaBanco!= null ){
                pecaBanco.setNome(peca.getNome());
                pecaBanco.setPreco(peca.getPreco());
                pecaBanco.setMarca(peca.getMarca());
                pecaBanco.setQuantidade(peca.getQuantidade());
                pecaBanco.setDescricao(peca.getDescricao());
                pecaBanco.setStatuspeca(peca.getStatuspeca());
                pecaRepository.save(pecaBanco);
                return  ResponseEntity.ok().build();
            }
            return  ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping("/{id}/excluir")
    @Operation(
            summary = "Método de exclusão de peça",
            description = "Método responsável por alterar o status da peça para EXCLUIDO")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        Peca pecaBanco = pecaRepository.findById(id).orElse(null);
        if(pecaBanco != null ){
            pecaBanco.setStatuspeca(EnumStatusPeca.EXCLUIDO);
            pecaRepository.save(pecaBanco);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}