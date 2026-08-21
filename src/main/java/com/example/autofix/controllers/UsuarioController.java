package com.example.autofix.controllers;

import com.example.autofix.entities.Usuario;
import com.example.autofix.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Grupo de APIs responsavel por controlar a estrutura de criação e consulta de usuários de sistema")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    @Operation(summary = "Metodo de consulta de lista de usuários!",
            description = "Metodo para responsavel por efetuar a consulta de todos os usuários sem filtro!")
    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Metodo de criação de usuários!",
            description = "Metodo para responsavel em efetuar a criação de novos usuários !")
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario){

        var usuarioBanco =  usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioBanco);
    }

}
