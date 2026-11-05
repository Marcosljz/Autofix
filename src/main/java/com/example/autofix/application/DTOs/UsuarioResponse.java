package com.example.autofix.application.DTOs;

import com.example.autofix.domain.entities.EnumStatusUsuario;
import com.example.autofix.domain.entities.Usuario;

public record UsuarioResponse(Long id, String nome, String cpf, String email, EnumStatusUsuario statusUsuario)  {

    public UsuarioResponse(Usuario usuarioEntidade){

        this(
                usuarioEntidade.getId(),
                usuarioEntidade.getNome(),
                usuarioEntidade.getCpf().toString(),
                usuarioEntidade.getEmail(),
                usuarioEntidade.getStatus()
        );
    }

}
