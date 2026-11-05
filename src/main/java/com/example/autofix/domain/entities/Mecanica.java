package com.example.autofix.domain.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

public class Mecanica {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO.IDENTITY)
    private Long id;

    private String razaoSocial;

    private String cnpj;

    private String nomeFantasia;

    @OneToMany(mappedBy = "Mecanica")
    private List<Usuario> usuarios;
}
