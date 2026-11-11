package com.example.autofix.domain.entities;

import com.example.autofix.application.DTOs.CriarAdminMecanicaRequest;
import com.example.autofix.application.DTOs.CriarAdminRequest;
import com.example.autofix.application.DTOs.EnumStatusUsuario;
import com.example.autofix.application.DTOs.UsuarioRequest;
import com.example.autofix.domain.valueobject.CPF;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity

@Data

@NoArgsConstructor

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String nome;

    @Embedded
    public CPF cpf;

    public String senha;

    @Column(unique = true)
    public String email;

    private String role = "ROLE_USER";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mecanica_id",referencedColumnName = "id")
    private Mecanica mecanica;


    public void setEmail(String email) {
        if(!email.isEmpty() && !email.contains("@")){
            throw new IllegalArgumentException("Email Invalido!");
        }
        this.email = email;
    }

    private EnumStatusUsuario status = EnumStatusUsuario.ATIVO;

    public Usuario(CriarAdminRequest criarAdminRequest) {
        this.setCpf( new CPF(criarAdminRequest.cpf()));
        this.setNome(criarAdminRequest.nome());
        this.setSenha(criarAdminRequest.senha());
        this.setEmail(criarAdminRequest.email());
        this.setRole("ROLE_ADMIN");

    }

    public Usuario(UsuarioRequest usuario) {
        this.setCpf( new CPF(usuario.cpf()));
        this.setNome(usuario.nome());
        this.setSenha(usuario.senha());
        this.setEmail(usuario.email());
        this.setRole("ROLE_USER");
    }

    public Usuario(CriarAdminMecanicaRequest request) {
        this.setCpf(new CPF(request.cpf()));
        this.setNome(request.nome());
        this.setSenha(request.senha());
        this.setEmail(request.email());
        this.setRole("ROLE_MECANICA");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

}