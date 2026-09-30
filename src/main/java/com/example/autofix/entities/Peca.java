package com.example.autofix.entities;

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

    public String quantidade;

    public String descricao;

    @Enumerated(EnumType.STRING)
    public EnumStatusPeca statuspeca = EnumStatusPeca.DISPONIVEL;

}
