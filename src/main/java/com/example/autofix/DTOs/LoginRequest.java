package com.example.autofix.DTOs;

// DTO (Data Transfer Object): representa exatamente os dados que o front-end manda no corpo
// da requisição de login (JSON com "email" e "senha"). Não é uma entidade do banco, é só
// um "contrato" de entrada/saída da API.
//
// "record" é um recurso do Java que já vem com construtor, getters (email(), senha()),
// equals, hashCode e toString prontos, sem precisar escrever nada disso -- ideal pra
// classes que só guardam dados, como esse DTO.
public record LoginRequest(String email , String senha) {
}