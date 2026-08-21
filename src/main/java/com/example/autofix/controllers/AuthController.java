package com.example.autofix.controllers;

import com.example.autofix.DTOs.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    @Tag(description = "Controller de autenticação", name = "Autenticação")
    @Operation(description = "Metodo de login", summary = "Autenticação de usuários")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        if (loginRequest.email().equals("string")&& loginRequest.senha().equals("string")){
            //Gerar Token
            return ResponseEntity.ok("");
        }
        return ResponseEntity.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();
    }

}
