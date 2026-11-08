package com.example.autofix.domain.entities;

import com.example.autofix.application.DTOs.EnumStatusPeca;
import com.example.autofix.application.DTOs.PecaRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Peca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String nome;

    public String preco;

    public String marca;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mecanica_id", referencedColumnName = "id")
    private Mecanica mecanica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cadastro_id", referencedColumnName = "id")
    private Usuario usuarioCadastro;

    public String quantidade;

    public String descricao;

    @Enumerated(EnumType.STRING)
    public EnumStatusPeca statuspeca = EnumStatusPeca.DISPONIVEL;


    public Peca(PecaRequest peca) {
        this.setNome(peca.nome());
        this.setPreco(peca.preco());
        this.setMarca(peca.Marca());
        this.setQuantidade(peca.quantidade());
        this.setDescricao(peca.Descricao());
        this.setStatuspeca(EnumStatusPeca.DISPONIVEL);
    }
}
