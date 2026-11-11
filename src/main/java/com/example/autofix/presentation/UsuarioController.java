package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.*;
import com.example.autofix.application.services.UsuarioService;
import com.example.autofix.application.DTOs.EnumStatusUsuario;

import com.example.autofix.domain.repository.UsuarioRepository;
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


    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Metodo de consulta de lista de usuários!",
            description = "Metodo para responsavel por efetuar a consulta de todos os usuários sem filtro!")
    public ResponseEntity<List<UsuarioResponse>> ListarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodosUsuarioTable());
    }
    @PostMapping("/admin")
    public ResponseEntity<CriarAdminResponse> criarAdmin(@RequestBody CriarAdminRequest criarAdminRequest){

        try {
            CriarAdminResponse respostaSalvar = usuarioService.criarAdmin(criarAdminRequest);
            return ResponseEntity.ok(respostaSalvar);

        }catch (RuntimeException e ){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/criar-mecanica")
    public ResponseEntity<?> criarMecanica (
            @RequestBody CriarAdminMecanicaRequest request) {

        try {
            CriarAdminMecanicaResponse resposta =
                    usuarioService.criarMecanicaComAdministrador(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta de usuário por ID",
            description = "Método responsável por buscar um usuário específico através do seu ID.")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id){

        UsuarioResponse usuarioBanco = usuarioService.buscarPorId(id);
        if(usuarioBanco!= null ){
            return  ResponseEntity.ok(usuarioBanco);
        }

        return  ResponseEntity.notFound().build(); // não achou -> 404
    }


    @PostMapping
    @Operation(summary = "Metodo de criação de usuários!",
            description = "Metodo para responsavel em efetuar a criação de novos usuários !")
    public ResponseEntity<?> criar(@RequestBody UsuarioRequest usuario){

        try {
            UsuarioResponse usuarioBanco = usuarioService.salvarUsuario(usuario, 0L);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusRequest statusRequest){

        var atualizarUsuario = usuarioService.atualizarStatusUsuario(statusRequest.status(),id);

        if(atualizarUsuario){
            return ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody UsuarioRequest usuario){

        try {
            UsuarioResponse usuarioBanco = usuarioService.salvarUsuario(usuario,id);
            return ResponseEntity.ok(usuarioBanco);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        var atualizarUsuario = usuarioService.atualizarStatusUsuario(EnumStatusUsuario.EXCLUIDO,id);

        if(atualizarUsuario){
            return ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();

    }
   @GetMapping("/buscarusuariologado")
    @Operation(summary = "Usuario Logado",description = "Usuario Logado")
    public ResponseEntity<UsuarioResponse> buscarusuariologado(){
        return ResponseEntity.ok(usuarioService.BuscarUsuariologado());
   }

}