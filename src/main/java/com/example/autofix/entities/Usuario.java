package com.example.autofix.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

// @Entity = essa classe representa uma tabela no banco de dados (JPA cria/mapeia a tabela "usuario").
@Entity
// @Data (Lombok) gera automaticamente getters, setters, toString, equals e hashCode -- sem precisar escrever na mão.
@Data
// Gera um construtor sem argumentos (obrigatório pro JPA conseguir instanciar a entidade).
@NoArgsConstructor
// Gera um construtor com todos os campos.
@AllArgsConstructor
public class Usuario {

    @Id // marca esse campo como a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // o próprio banco gera o valor do id automaticamente (auto-incremento)
    public Long id;

    public String nome;

    public String cpf;

    public String senha;

    public String email;

    // Valor padrão: todo usuário nasce com status ATIVO, a não ser que seja alterado depois.
    private EnumStatusUsuario status = EnumStatusUsuario.ATIVO;
}