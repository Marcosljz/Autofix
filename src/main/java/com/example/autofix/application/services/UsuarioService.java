package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.*;
import com.example.autofix.domain.entities.EnumStatusUsuario;
import com.example.autofix.domain.entities.Usuario;
import com.example.autofix.domain.repository.UsuarioRepository;
import com.example.autofix.domain.valueobject.CPF;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Value("${spring.secret}")
    private String secret;


    public LoginResponse validarUsuarioAutenticadoRetornaToken(LoginRequest loginRequest) {


        if (usuarioRepository.existsUsuarioByEmailAndSenha(loginRequest.email(), loginRequest.senha())) {

            var token = tokenService.gerarToken(loginRequest);

            return new LoginResponse(token);
        }

        return null;
    }

    public List<UsuarioResponse> listarTodosUsuarioTable(){

        var usuarioLogado = getUsuarioLogado();

        if (usuarioLogado.mecanica_id() == null){
            return usuarioRepository.findAll()
                    .stream()
                    .map(UsuarioResponse::new)
                    .toList();
        }

        return usuarioRepository.getUsuariosByEmpresa_Id(usuarioLogado.mecanica_id())
                .stream()
                .map(UsuarioResponse::new)
                .toList();

    }



    public CriarAdminResponse criarAdmin(CriarAdminRequest criarAdminRequest) {

        if (!criarAdminRequest.secretKey().equals(secret)) {
            return new CriarAdminResponse(0L, "Usuario Salvo com sucesso!");
        }
        Usuario usuarioAdminSalvar = new Usuario(criarAdminRequest);

        usuarioRepository.save(usuarioAdminSalvar);

        return new CriarAdminResponse(usuarioAdminSalvar.getId(), "Usuario Salvo com sucesso!");

    }

    public UsuarioLogado getUsuarioLogado() {
        return (UsuarioLogado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public UsuarioResponse salvarUsuario(UsuarioRequest usuario, Long id) {

        var usuarioLogado = getUsuarioLogado();
        if (id == 0L){
            Usuario usuarioBanco = new Usuario(usuario);
            usuarioRepository.save(usuarioBanco);

            return new UsuarioResponse(usuarioBanco);
        } else {
            Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
            if ( usuarioBanco != null ){
                usuarioBanco.setCpf(new CPF(usuario.cpf()));
                usuarioBanco.setEmail(usuario.email());
                usuarioBanco.setNome(usuario.nome());
                usuarioBanco.setSenha(usuario.senha());
                usuarioRepository.save(usuarioBanco);
                return new UsuarioResponse(usuarioBanco);
            }
            return null;
        }
    }


    public UsuarioResponse buscarPorId(Long id) {

        var usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponse(usuario);


    }

    public boolean atualizarStatus(EnumStatusUsuario status, Long id) {

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);
        if (usuarioBanco != null) {
            usuarioBanco.setStatus(status);
            usuarioRepository.save(usuarioBanco);
            return true;
        }
        return false;

    }

    public UsuarioResponse BuscarUsuariologado() {
        try {
            var usuariologado = getUsuarioLogado();
            return usuarioRepository.findById(usuariologado.id())
                    .stream()
                    .map(UsuarioResponse::new).findFirst().orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Usuario Logado!");
        }
    }
}