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

// Marca a classe como controller REST: recebe requisições HTTP e devolve os dados direto como JSON (sem renderizar página HTML)
@RestController
public class AuthController {


    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/login")

    @Tag(description = "Controller de autenticação", name = "Autenticação")
    @Operation(description = "Metodo de login", summary = "Autenticação de usuários")

    public ResponseEntity<?> login(@RequestBody LoginRequest Request) {

        var resultadoAutenticaoToken = usuarioService.validarUsuarioAutenticadoRetornaToken(Request);

        if (resultadoAutenticaoToken !=null) {
            return ResponseEntity.ok(resultadoAutenticaoToken);
        }

        return ResponseEntity.badRequest().body("Usuário ou senha Invalido!");
    }
}
