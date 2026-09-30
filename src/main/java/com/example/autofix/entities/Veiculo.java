package com.example.autofix.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String placa;

    public String modelo;

    public String ano;

    public String cor;

    public Double quilometragem;

    @Enumerated(EnumType.STRING)
    public EnumStatusVeiculo statusVeiculo = EnumStatusVeiculo.ATIVO;
}
