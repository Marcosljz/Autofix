package com.example.autofix.controllers;

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
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;
    @Tag(
            name = "Veículos",
            description = "Grupo de APIs responsável pelo controle e gerenciamento de veículos")

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

}
