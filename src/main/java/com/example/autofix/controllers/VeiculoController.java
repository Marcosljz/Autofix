package com.example.autofix.controllers;

import com.example.autofix.DTOs.AtualizarStatusVeiculoRequest;
import com.example.autofix.entities.EnumStatusVeiculo;
import com.example.autofix.entities.Usuario;
import com.example.autofix.entities.Veiculo;
import com.example.autofix.repository.UsuarioRepository;
import com.example.autofix.repository.VeiculoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculos")
@Tag(
        name = "Veículos",
        description = "Grupo de APIs responsável pelo controle e gerenciamento de veículos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @GetMapping
    @Operation(summary = "Método de consulta de veículos",
            description = "Método responsável por consultar todos os veículos cadastrados no sistema")
    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(veiculoRepository.findAll());
    }

    @PostMapping
    @Operation(
            summary = "Método de criação de veículo",
            description = "Método responsável por cadastrar um novo veículo no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Veiculo> criar(@RequestBody Veiculo veiculo){

        var veiculoBanco = veiculoRepository.save(veiculo);
        return ResponseEntity.ok(veiculoBanco);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Método de atualização de status de veículo",
            description = "Método responsável por atualizar apenas o status de um veículo cadastrado no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusVeiculoRequest statusRequest){

        Veiculo veiculoBanco = veiculoRepository.findById(id).orElse(null);
        if(veiculoBanco!= null ){
            veiculoBanco.setStatusVeiculo(statusRequest.statusVeiculo());
            veiculoRepository.save(veiculoBanco);
            return  ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Método de atualização de veículo",
            description = "Método responsável por atualizar todos os dados de um veículo cadastrado no sistema")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long id, @RequestBody Veiculo veiculo){

        try{
            Veiculo veiculoBanco = veiculoRepository.findById(id).orElse(null);
            if(veiculoBanco!= null ){
                veiculoBanco.setPlaca(veiculo.getPlaca());
                veiculoBanco.setModelo(veiculo.getModelo());
                veiculoBanco.setAno(veiculo.getAno());
                veiculoBanco.setCor(veiculo.getCor());
                veiculoBanco.setQuilometragem(veiculo.getQuilometragem());
                veiculoBanco.setStatusVeiculo(veiculo.getStatusVeiculo());
                veiculoRepository.save(veiculoBanco);
                return  ResponseEntity.ok().build();
            }
            return  ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping("/{id}/excluir")
    @Operation(
            summary = "Método de exclusão de veículo",
            description = "Método responsável por excluir um veículo cadastrado no sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        Veiculo veiculoBanco = veiculoRepository.findById(id).orElse(null);
        if(veiculoBanco!= null ){
            veiculoBanco.setStatusVeiculo(EnumStatusVeiculo.EXCLUIDO);
            veiculoRepository.save(veiculoBanco);
            return  ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

}
