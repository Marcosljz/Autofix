package com.example.autofix.DTOs;

// DTO de saída do login: é exatamente o formato do JSON que a API devolve pro front
// depois de um login bem-sucedido -- só o token JWT gerado.
// O front vai guardar esse token (geralmente no localStorage ou num cookie) e reenviar
// ele em toda requisição futura, no header "Authorization: Bearer <token>".
public record LoginResponse(String token) {
}