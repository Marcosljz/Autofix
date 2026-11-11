package com.example.autofix.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class OrdemServicoPeca {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ordem_servico_id", referencedColumnName = "id")
        private OrdemServico ordemServico;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "peca_id", referencedColumnName = "id")
        private Peca peca;

        private Integer quantidade;
    }

