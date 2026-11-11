
package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.*;
import com.example.autofix.application.DTOs.EnumStatusUsuario;
import com.example.autofix.domain.entities.Mecanica;
import com.example.autofix.domain.entities.Usuario;
import com.example.autofix.domain.repository.MecanicaRepository;
import com.example.autofix.domain.repository.UsuarioRepository;
import com.example.autofix.domain.valueobject.CPF;
import jakarta.transaction.Transactional;
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

    @Autowired
    private MecanicaRepository mecanicaRepository;

    @Autowired
    private ContextoSegurancaService contexto;


    public LoginResponse validarUsuarioAutenticadoRetornaToken(LoginRequest loginRequest) {

        if (usuarioRepository.existsUsuarioByEmailAndSenha(loginRequest.email(), loginRequest.senha())) {

            var token = tokenService.gerarToken(loginRequest);

            return new LoginResponse(token);
        }

        return null;
    }


    public List<UsuarioResponse> listarTodosUsuarioTable() {

        List<Usuario> usuarios;

        if (contexto.isAdmin()) {

            // Administrador geral visualiza todos os usuários
            usuarios = usuarioRepository.findAll();

        } else if (contexto.isAdminMecanica()) {

            // Administrador da mecânica visualiza os usuários da própria empresa
            Long mecanicaId = contexto.getMecanicaId();

            if (mecanicaId == null) {
                return List.of();
            }

            usuarios = usuarioRepository.getUsuariosBymecanica_id(mecanicaId)
                    .stream()
                    .filter(usuario -> usuario.getStatus() != EnumStatusUsuario.EXCLUIDO)
                    .toList();

        } else {
            return List.of();
        }

        return usuarios.stream()
                .map(UsuarioResponse::new)
                .toList();
    }


    private boolean podeAcessar(Usuario usuario) {

        if (contexto.isAdmin()) {
            return true;
        }

        return usuario.getMecanica() != null
                && contexto.podeAcessar(usuario.getMecanica());
    }


    public UsuarioResponse buscarPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            return null;
        }

        // Administrador geral pode buscar qualquer usuário
        if (contexto.isAdmin()) {
            return new UsuarioResponse(usuario);
        }

        // Administrador da mecânica só busca usuários da própria mecânica
        if (contexto.isAdminMecanica() && podeAcessar(usuario)
                && usuario.getStatus() != EnumStatusUsuario.EXCLUIDO) {
            return new UsuarioResponse(usuario);
        }

        // Funcionário comum não pode consultar usuários
        return null;
    }


    public CriarAdminResponse criarAdmin(CriarAdminRequest criarAdminRequest) {

        if (!criarAdminRequest.secretKey().equals(secret)) {
            throw new RuntimeException("Chave de segurança inválida!");
        }

        Usuario usuarioAdminSalvar = new Usuario(criarAdminRequest);

        validarEmailDisponivel(usuarioAdminSalvar.getEmail(), null);

        usuarioRepository.save(usuarioAdminSalvar);

        return new CriarAdminResponse(
                usuarioAdminSalvar.getId(),
                "Usuario Salvo com sucesso!"
        );
    }


    @Transactional
    public CriarAdminMecanicaResponse criarMecanicaComAdministrador(CriarAdminMecanicaRequest request) {

        if (!contexto.isAdmin()) {
            throw new RuntimeException(
                    "Somente o administrador geral pode criar mecânicas!"
            );
        }

        if (!secret.equals(request.secretKey())) {
            throw new RuntimeException("Chave secreta inválida!");
        }

        Mecanica mecanica = new Mecanica();
        mecanica.setRazaoSocial(request.razaoSocial());
        mecanica.setNomeFantasia(request.nomeFantasia());
        mecanica.setCnpj(request.cnpj());

        mecanica = mecanicaRepository.save(mecanica);

        Usuario administrador = new Usuario(request);

        validarEmailDisponivel(administrador.getEmail(), null);

        administrador.setMecanica(mecanica);

        usuarioRepository.save(administrador);

        return new CriarAdminMecanicaResponse(
                administrador.getId(),
                mecanica.getId(),
                "Mecânica e administrador cadastrados com sucesso!"
        );
    }


    public UsuarioLogado getUsuarioLogado() {

        return (UsuarioLogado) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }


    private void validarEmailDisponivel(String email, Long id) {

        boolean emailExiste;

        if (id == null) {
            emailExiste = usuarioRepository.existsByEmail(email);
        } else {
            emailExiste = usuarioRepository.existsByEmailAndIdNot(email, id);
        }

        if (emailExiste) {
            throw new RuntimeException("Este e-mail já está cadastrado no sistema!");
        }
    }

    public UsuarioResponse salvarUsuario(UsuarioRequest usuario, Long id) {

        // Cadastro de funcionário: somente administrador da mecânica
        if (id == 0L) {

            if (!contexto.isAdminMecanica()) {
                throw new RuntimeException(
                        "Você não tem permissão para cadastrar usuários!"
                );
            }

            Mecanica mecanica = contexto.getMecanicaDoLogado();

            if (mecanica == null) {
                throw new RuntimeException(
                        "Sua mecânica não está vinculada ao usuário!"
                );
            }

            Usuario novoUsuario = new Usuario(usuario);

            validarEmailDisponivel(novoUsuario.getEmail(), null);

            novoUsuario.setMecanica(mecanica);

            usuarioRepository.save(novoUsuario);

            return new UsuarioResponse(novoUsuario);
        }

        // Busca usuário para edição
        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco == null) {
            throw new RuntimeException("Usuário não encontrado!");
        }

        // Administrador geral só pode editar administradores de mecânicas
        if (contexto.isAdmin()) {

            if (!"ROLE_MECANICA".equals(usuarioBanco.getRole())) {
                throw new RuntimeException(
                        "O administrador geral só pode editar administradores de mecânicas!"
                );
            }

        } else if (contexto.isAdminMecanica()) {

            // Administrador da mecânica só pode editar funcionários
            if (!"ROLE_USER".equals(usuarioBanco.getRole())) {
                throw new RuntimeException(
                        "Você não pode editar este tipo de usuário!"
                );
            }

            // Verifica se pertence à mesma mecânica
            if (usuarioBanco.getMecanica() == null
                    || !usuarioBanco.getMecanica().getId().equals(contexto.getMecanicaId())) {

                throw new RuntimeException(
                        "Este usuário não pertence à sua mecânica!"
                );
            }

        } else {
            throw new RuntimeException(
                    "Você não tem permissão para editar usuários!"
            );
        }

        // Verifica se o e-mail pertence a outro usuário
        validarEmailDisponivel(usuario.email(), usuarioBanco.getId());

        usuarioBanco.setCpf(new CPF(usuario.cpf()));
        usuarioBanco.setEmail(usuario.email());
        usuarioBanco.setNome(usuario.nome());
        usuarioBanco.setSenha(usuario.senha());

        usuarioRepository.save(usuarioBanco);

        return new UsuarioResponse(usuarioBanco);
    }


    public boolean atualizarStatusUsuario(EnumStatusUsuario status, Long id) {

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco == null) {
            return false;
        }

        // Administrador geral pode alterar status de administradores de mecânicas
        if (contexto.isAdmin()) {

            if (!"ROLE_MECANICA".equals(usuarioBanco.getRole())) {
                throw new RuntimeException(
                        "O administrador geral só pode alterar o status dos administradores de mecânicas!"
                );
            }

        } else if (contexto.isAdminMecanica()) {

            // Impede alterar o próprio usuário
            if (usuarioBanco.getId().equals(contexto.getUsuarioLogadoId())) {
                throw new RuntimeException(
                        "Você não pode alterar o status da própria conta!"
                );
            }

            // Só permite alterar funcionários ROLE_USER
            if (!"ROLE_USER".equals(usuarioBanco.getRole())) {
                throw new RuntimeException(
                        "Você não pode alterar o status deste usuário!"
                );
            }

            // Verifica se pertence à própria mecânica
            if (usuarioBanco.getMecanica() == null
                    || !usuarioBanco.getMecanica().getId().equals(contexto.getMecanicaId())) {

                throw new RuntimeException(
                        "Este usuário não pertence à sua mecânica!"
                );
            }

        } else {
            throw new RuntimeException(
                    "Você não tem permissão para alterar o status dos usuários!"
            );
        }

        usuarioBanco.setStatus(status);

        usuarioRepository.save(usuarioBanco);

        return true;
    }


    public UsuarioResponse BuscarUsuariologado() {

        try {
            var usuariologado = getUsuarioLogado();

            return usuarioRepository.findById(usuariologado.id())
                    .stream()
                    .map(UsuarioResponse::new)
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Usuario Logado!");
        }
    }

}
