package com.example.autofix.controllers;

import com.example.autofix.DTOs.AtualizarStatusRequest;
import com.example.autofix.entities.EnumStatusUsuario;
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusRequest statusRequest){

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
        if(usuarioBanco!= null ){
            usuarioBanco.setStatus(statusRequest.status());
            usuarioRepository.save(usuarioBanco);
            return  ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuario){

        try{
            Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
            if(usuarioBanco!= null ){
                usuarioBanco.setStatus(usuario.getStatus());
                usuarioBanco.setNome(usuario.getNome());
                usuarioBanco.setCpf(usuario.getCpf());
                usuarioBanco.setEmail(usuario.getEmail());
                usuarioBanco.setSenha(usuario.getSenha());
                usuarioRepository.save(usuarioBanco);
                return  ResponseEntity.ok().build();
            }
            return  ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
        if(usuarioBanco!= null ){
            usuarioBanco.setStatus(EnumStatusUsuario.EXCLUIDO);
            usuarioRepository.save(usuarioBanco);
            return  ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }


}
