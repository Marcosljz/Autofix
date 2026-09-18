package com.example.autofix.controllers;

import com.example.autofix.DTOs.*;
import com.example.autofix.entities.Usuario;
import com.example.autofix.repository.UsuarioRepository;
import com.example.autofix.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.util.UUID;

@RestController
public class AuthController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @PostMapping("/login")

    @Tag(description = "Controller de autenticação", name = "Autenticação")
    @Operation(description = "Metodo de login", summary = "Autenticação de usuários")

    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        if (usuarioRepository.existsUsuarioByEmailAndSenha(loginRequest.email(), loginRequest.senha())) {

            var token = tokenService.gerarToken(loginRequest.email());

            return ResponseEntity.ok(new LoginResponse(token));
        }

        return ResponseEntity.badRequest().body("Usuário ou senha Invalido!");

    }

    @Operation(description = "Método responsável por gerar um token de recuperação de senha para o e-mail informado",
            summary = "Esqueci minha senha")
    @PostMapping("/esqueci-senha")
    public ResponseEntity<?> esqueciSenha(@RequestBody EsqueciSenhaRequest esqueciSenhaRequest){

        Usuario usuarioBanco = usuarioRepository.findByEmail(esqueciSenhaRequest.email()).orElse(null);

        if (usuarioBanco == null) {
            return ResponseEntity.badRequest().body("E-mail não encontrado!");
        }

        String token = UUID.randomUUID().toString();

        usuarioBanco.setTokenRecuperacaoSenha(token);
        usuarioRepository.save(usuarioBanco);

        // Enquanto não há serviço de e-mail configurado, o token é devolvido na resposta.
        // Quando o envio de e-mail for implementado, troque a linha abaixo por um EmailService.enviar(...)
        // e retorne apenas uma confirmação, sem expor o token.
        return ResponseEntity.ok(new EsqueciSenhaResponse(token));
    }

    @Operation(description = "Método responsável por validar o token de recuperação e definir uma nova senha",
            summary = "Redefinir senha")
    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody RedefinirSenhaRequest redefinirSenhaRequest){

        Usuario usuarioBanco = usuarioRepository.findByEmail(redefinirSenhaRequest.email()).orElse(null);

        if (usuarioBanco == null) {
            return ResponseEntity.badRequest().body("E-mail não encontrado!");
        }

        if (usuarioBanco.getTokenRecuperacaoSenha() == null
                || !usuarioBanco.getTokenRecuperacaoSenha().equals(redefinirSenhaRequest.token())) {
            return ResponseEntity.badRequest().body("Token inválido!");
        }

        usuarioBanco.setSenha(redefinirSenhaRequest.novaSenha());
        usuarioBanco.setTokenRecuperacaoSenha(null);
        usuarioRepository.save(usuarioBanco);

        return ResponseEntity.ok().build();
    }

}
