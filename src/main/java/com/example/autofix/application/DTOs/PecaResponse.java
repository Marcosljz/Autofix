package com.example.autofix.application.DTOs;

import com.example.autofix.domain.entities.EnumStatusPeca;
import com.example.autofix.domain.entities.Peca;

public record PecaResponse(Long id, String nome, String preco, String marca, String quantidade, String descricao, EnumStatusPeca statusPeca) {
    public PecaResponse(Peca pecaEntidade) {
        this(
                pecaEntidade.getId(),
                pecaEntidade.getNome(),
                pecaEntidade.getPreco(),
                pecaEntidade.getMarca(),
                pecaEntidade.getQuantidade(),
                pecaEntidade.getDescricao(),
                pecaEntidade.getStatuspeca()
        );
    }
}
