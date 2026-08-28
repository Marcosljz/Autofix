package com.example.autofix.controllers;

import com.example.autofix.entities.Peca;
import com.example.autofix.entities.Usuario;
import com.example.autofix.repository.PecaRepository;
import com.example.autofix.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    @Autowired
    private PecaRepository pecaRepository;
    @Tag(name = "Peças",
            description = "Grupo de APIs responsável pelo controle e gerenciamento de peças")
    @GetMapping
    @Operation(
            summary = "Método de consulta de peças",
            description = "Método responsável por consultar todas as peças cadastradas no sistema")

    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(pecaRepository.findAll());
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

}
