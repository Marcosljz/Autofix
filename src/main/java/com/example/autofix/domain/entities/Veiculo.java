package com.example.autofix.domain.entities;

import com.example.autofix.application.DTOs.EnumStatusVeiculo;
import com.example.autofix.application.DTOs.VeiculoRequest;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mecanica_id", referencedColumnName = "id")
    private Mecanica mecanica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cadastro_id", referencedColumnName = "id")
    private Usuario usuarioCadastro;

    @Enumerated(EnumType.STRING)
    public EnumStatusVeiculo statusVeiculo = EnumStatusVeiculo.ATIVO;


    public Veiculo(VeiculoRequest veiculo) {
        this.setPlaca(veiculo.placa());
        this.setModelo(veiculo.Modelo());
        this.setAno(veiculo.ano());
        this.setCor(veiculo.cor());
        this.setQuilometragem(veiculo.quilometragem());
        this.setStatusVeiculo(EnumStatusVeiculo.ATIVO);
    }




}

