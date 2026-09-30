package com.example.autofix.entities;

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

    public LocalDateTime dataConclusao;

    @Enumerated(EnumType.STRING)
    public EnumStatusOrdemServico statusOrdemServico =EnumStatusOrdemServico.ABERTO;

    public String descricao;



}
