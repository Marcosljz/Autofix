package com.example.autofix.domain.entities;

import com.example.autofix.application.DTOs.EnumStatusOrdemServico;
import com.example.autofix.application.DTOs.OrdemServicoRequest;
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


    private boolean estoqueBaixado = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mecanica_id", referencedColumnName = "id")
    private Mecanica mecanica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cadastro_id", referencedColumnName = "id")
    private Usuario usuarioCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", referencedColumnName = "id")
    private Veiculo veiculo;

    @Enumerated(EnumType.STRING)
    public EnumStatusOrdemServico statusOrdemServico =EnumStatusOrdemServico.ABERTO;

    public String descricao;


    public OrdemServico(OrdemServicoRequest ordemServico) {
        this.setDataAbertura(ordemServico.dataAbertura());
        this.setDataConclusao(ordemServico.dataConclusao());
        this.setDescricao(ordemServico.descricao());
        this.setStatusOrdemServico(EnumStatusOrdemServico.ABERTO);
    }
}
