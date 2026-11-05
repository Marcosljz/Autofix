package com.example.autofix.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public LocalDateTime dataAbertura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mecanica_id", nullable = false)
    private Mecanica mecanica;

    public LocalDateTime dataConclusao;

    @Enumerated(EnumType.STRING)
    public EnumStatusOrdemServico statusOrdemServico =EnumStatusOrdemServico.ABERTO;

    public String descricao;



}
