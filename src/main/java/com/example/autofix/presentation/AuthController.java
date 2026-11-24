package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.LoginRequest;
import com.example.autofix.application.DTOs.LoginResponse;

import com.example.autofix.application.services.TokenService;
import com.example.autofix.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.autofix.application.DTOs.ErroResponse;

// Marca a classe como controller REST: recebe requisições HTTP e devolve os dados direto como JSON (sem renderizar página HTML)
@RestController
@Tag(name = "Autenticação", description = "Controller responsável pela autenticação dos usuários")
public class AuthController {


    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/login")

    @Operation(
            summary = "Autenticação de usuários",
            description = "Realiza o login do usuário e retorna o token de autenticação"
    )

    public ResponseEntity<?> login(@RequestBody LoginRequest Request) {

        var resultadoAutenticaoToken = usuarioService.validarUsuarioAutenticadoRetornaToken(Request);

        if (resultadoAutenticaoToken !=null) {
            return ResponseEntity.ok(resultadoAutenticaoToken);
        }

        return ResponseEntity.badRequest()
                .body(new ErroResponse("Usuário ou senha inválidos!"));

    }
}
