package com.example.autofix.DTOs;

import com.example.autofix.entities.EnumStatusUsuario;

// DTO usado no PATCH /usuarios/{id}/status: representa o JSON que o front manda
// quando quer trocar SÓ o status de um usuário (ex: { "status": "BLOQUEADO" }).
// Por isso o PATCH só recebe esse campo, e não o objeto Usuario inteiro -- reforça
// a ideia de PATCH como atualização parcial (diferente do PUT, que usa o Usuario completo).
public record AtualizarStatusRequest(EnumStatusUsuario status) {


}