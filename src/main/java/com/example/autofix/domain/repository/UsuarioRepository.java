package com.example.autofix.domain.repository;

import com.example.autofix.application.DTOs.EnumStatusUsuario;
import com.example.autofix.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//trabalhar com ocp e liskov


// Repository = camada que fala com o banco de dados.
// Ao extender JpaRepository<Usuario, Long>, já ganha de graça métodos prontos
// como findAll(), findById(), save(), delete() — sem precisar escrever nenhum SQL.
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    // "Derived query": o Spring Data lê o NOME do método e monta a query sozinho.
    // Esse aqui vira: "existe um Usuario com esse email E essa senha?" -> usado no login.
    boolean existsUsuarioByEmailAndSenha(String email, String senha);


    // "Busque todos os Usuarios cujo status seja DIFERENTE (Not) do informado."
    // Usado pra listar usuários ativos, por exemplo, excluindo os que estão EXCLUIDO.
    Optional<List<Usuario>> findByStatusNot(EnumStatusUsuario status);

    List<Usuario> getUsuariosBymecanica_id(long mecanica);
}