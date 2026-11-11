package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.*;
import com.example.autofix.application.services.VeiculoService;
import com.example.autofix.application.DTOs.EnumStatusVeiculo;
import com.example.autofix.domain.repository.VeiculoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@Tag(
        name = "Veículos",
        description = "Grupo de APIs responsável pelo controle e gerenciamento de veículos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    @Operation(summary = "Método de consulta de veículos",
            description = "Método responsável por consultar todos os veículos cadastrados no sistema")
    public ResponseEntity<List<VeiculoResponse>> listarTodos() {

        return ResponseEntity.ok(veiculoService.listarTodosVeiculosTable());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta de veículos por ID",
            description = "Método responsável por buscar um veículos específico através do seu ID.")
    public ResponseEntity<VeiculoResponse> buscarPorId (@PathVariable Long id){

        VeiculoResponse veiculoBanco = veiculoService.buscarPorId(id);
        if(veiculoBanco!= null ){
            return  ResponseEntity.ok(veiculoBanco);
        }

        return  ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
            summary = "Método de criação de veículo",
            description = "Método responsável por cadastrar um novo veículo no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@RequestBody VeiculoRequest veiculo){

        try {
            VeiculoResponse veiculoBanco = veiculoService.salvarVeiculo(veiculo,0L);
            return ResponseEntity.ok(veiculoBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Método de atualização de status de veículo",
            description = "Método responsável por atualizar apenas o status de um veículo cadastrado no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusVeiculoRequest statusRequest){

        var atualizarVeiculo = veiculoService.atualizarStatus(statusRequest.statusVeiculo(),id);

        if(atualizarVeiculo){
            return ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Método de atualização de veículo",
            description = "Método responsável por atualizar todos os dados de um veículo cadastrado no sistema")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody VeiculoRequest veiculo){

        try {
            VeiculoResponse veiculoBanco = veiculoService.salvarVeiculo(veiculo,id);
            return ResponseEntity.ok(veiculoBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}/excluir")
    @Operation(
            summary = "Método de exclusão de veículo",
            description = "Método responsável por excluir um veículo cadastrado no sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        var atualizarVeiculo = veiculoService.atualizarStatus(EnumStatusVeiculo.EXCLUIDO,id);

        if(atualizarVeiculo){
            return ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();

    }

}
