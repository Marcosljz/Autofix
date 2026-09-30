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

// Controller REST responsável pelo CRUD de usuários. Todas as rotas aqui começam com /usuarios (@RequestMapping).
@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Grupo de APIs responsavel por controlar a estrutura de criação e consulta de usuários de sistema")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // GET /usuarios -> lista todos os usuários cadastrados, sem filtro nenhum.
    @GetMapping
    @Operation(summary = "Metodo de consulta de lista de usuários!",
            description = "Metodo para responsavel por efetuar a consulta de todos os usuários sem filtro!")
    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    // GET /usuarios/{id} -> busca um usuário específico pelo id.
    // @PathVariable pega o valor que vem na URL (ex: /usuarios/5 -> id = 5).
    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta de usuário por ID",
            description = "Método responsável por buscar um usuário específico através do seu ID.")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id){

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
        if(usuarioBanco!= null ){
            return  ResponseEntity.ok(usuarioBanco);
        }

        return  ResponseEntity.notFound().build(); // não achou -> 404
    }

    // POST /usuarios -> cria um novo usuário.
    // @RequestBody pega o JSON enviado no corpo da requisição e transforma num objeto Usuario.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // responde com status 201 (Created) quando dá certo
    @Operation(summary = "Metodo de criação de usuários!",
            description = "Metodo para responsavel em efetuar a criação de novos usuários !")
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario){

        var usuarioBanco =  usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioBanco);
    }

    // PATCH /usuarios/{id}/status -> atualiza SÓ o campo status do usuário (não mexe no resto dos dados).
    // É o padrão certo do PATCH: alterar parcialmente, diferente do PUT que atualiza tudo.
    @PatchMapping("/{id}/status")
    @Operation(summary = "Método de atualização do status do usuário",
            description = "Método responsável por alterar somente o status de um usuário através do seu ID.")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusRequest statusRequest){

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
        if(usuarioBanco!= null ){
            usuarioBanco.setStatus(statusRequest.status());
            usuarioRepository.save(usuarioBanco);
            return  ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

    // PUT /usuarios/{id} -> atualiza TODOS os dados do usuário (substitui o registro inteiro).
    @PutMapping("/{id}")
    @Operation(summary = "Método de atualização de usuário",
            description = "Método responsável por atualizar os dados de um usuário através do seu ID.")
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

    // DELETE /usuarios/{id}/excluir -> "exclui" o usuario SEM apagar do banco (soft delete):
    // só muda o status dele para EXCLUIDO. Assim mantém o histórico, mas ele some das listagens ativas.
    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Método de exclusão de usuário",
            description = "Método responsável por excluir um usuário de forma lógica, alterando seu status para EXCLUIDO sem apagar o registro do banco.")
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