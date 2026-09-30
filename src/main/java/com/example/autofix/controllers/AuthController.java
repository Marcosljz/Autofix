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
// Marca a classe como controller REST: recebe requisições HTTP e devolve os dados direto como JSON (sem renderizar página HTML)
@RestController
public class AuthController {

    // Injeção de dependência: o Spring cria e entrega essa instância pronta, sem precisar dar "new TokenService()" na mão
    @Autowired
    private TokenService tokenService;

    // Mesma ideia: o Spring injeta a instância do repositório de Usuario automaticamente
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Rota pública POST /login — é onde o usuário manda email+senha pra entrar no sistema.
    @PostMapping("/login")

    @Tag(description = "Controller de autenticação", name = "Autenticação")
    @Operation(description = "Metodo de login", summary = "Autenticação de usuários")

    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        // Verifica no banco se existe um usuário com esse email E essa senha exatos.
        if (usuarioRepository.existsUsuarioByEmailAndSenha(loginRequest.email(), loginRequest.senha())) {

            // Credenciais corretas -> gera um token JWT identificando esse usuário pelo email.
            var token = tokenService.gerarToken(loginRequest.email());

            // Devolve o token pro front-end (ele vai usar isso no header "Authorization: Bearer <token>"
            // em todas as próximas requisições, pra provar que está autenticado — é o que o JwtFilter confere).
            return ResponseEntity.ok(new LoginResponse(token));
        }

        // Email/senha não bateram -> devolve 400 (Bad Request) com uma mensagem de erro.
        return ResponseEntity.badRequest().body("Usuário ou senha Invalido!");
    }
}
