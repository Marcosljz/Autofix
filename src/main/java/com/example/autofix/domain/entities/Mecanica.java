package com.example.autofix.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mecanica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razaoSocial;

    private String cnpj;

    private String nomeFantasia;

    @OneToMany(mappedBy = "mecanica")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "mecanica")
    private List<Veiculo> veiculos;

    @OneToMany(mappedBy = "mecanica")
    private List<Peca> pecas;

    @OneToMany(mappedBy = "mecanica")
    private List<OrdemServico> ordensServico;

}