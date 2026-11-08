
package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.PecaResponse;
import com.example.autofix.application.DTOs.PecaRequest;
import com.example.autofix.application.DTOs.EnumStatusPeca;
import com.example.autofix.domain.entities.Mecanica;
import com.example.autofix.domain.entities.Peca;
import com.example.autofix.domain.repository.PecaRepository;
import com.example.autofix.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PecaService {

    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContextoSegurancaService contexto;


    public List<PecaResponse> listarTodasPecasTable() {

        List<Peca> pecas;

        if (contexto.isAdmin()) {

            pecas = pecaRepository.findAll();

        } else if (contexto.isAdminMecanica()) {

            Long mecanicaId = contexto.getMecanicaId();

            if (mecanicaId == null) {
                return List.of();
            }

            pecas = pecaRepository.findByMecanica_Id(mecanicaId);

        } else {

            Long usuarioId = contexto.getUsuarioLogadoId();

            if (usuarioId == null) {
                return List.of();
            }

            pecas = pecaRepository.findByUsuarioCadastro_Id(usuarioId);
        }

        return pecas.stream()
                .map(PecaResponse::new)
                .toList();
    }


    public PecaResponse buscarPorId(Long id) {

        var peca = pecaRepository.findById(id).orElse(null);

        if (peca == null) {
            return null;
        }

        if (contexto.isAdmin()) {
            return new PecaResponse(peca);
        }

        if (contexto.isAdminMecanica() &&
                contexto.podeAcessar(peca.getMecanica())) {
            return new PecaResponse(peca);
        }

        if (peca.getUsuarioCadastro() != null &&
                peca.getUsuarioCadastro().getId()
                        .equals(contexto.getUsuarioLogadoId())) {
            return new PecaResponse(peca);
        }

        return null;
    }


    public PecaResponse salvarPeca(PecaRequest peca, Long id) {

        if (id == 0L) {

            Mecanica mecanica = contexto.getMecanicaDoLogado();

            if (mecanica == null || contexto.isAdmin()) {
                throw new RuntimeException(
                        "Usuário não possui permissão para cadastrar peças!"
                );
            }

            Peca pecaBanco = new Peca(peca);

            pecaBanco.setMecanica(mecanica);

            pecaBanco.setUsuarioCadastro(
                    usuarioRepository.getReferenceById(
                            contexto.getUsuarioLogadoId()
                    )
            );

            pecaRepository.save(pecaBanco);

            return new PecaResponse(pecaBanco);

        } else {

            Peca pecaBanco = pecaRepository.findById(id).orElse(null);

            if (pecaBanco != null &&
                    !contexto.isAdmin() &&
                    podeGerenciarPeca(pecaBanco)) {

                pecaBanco.setNome(peca.nome());
                pecaBanco.setPreco(peca.preco());
                pecaBanco.setMarca(peca.Marca());
                pecaBanco.setQuantidade(peca.quantidade());
                pecaBanco.setDescricao(peca.Descricao());

                pecaRepository.save(pecaBanco);

                return new PecaResponse(pecaBanco);
            }
        }

        return null;
    }


    public boolean atualizarStatus(EnumStatusPeca statusPeca, Long id) {

        Peca pecaBanco = pecaRepository.findById(id).orElse(null);

        if (pecaBanco != null &&
                !contexto.isAdmin() &&
                podeGerenciarPeca(pecaBanco)) {

            pecaBanco.setStatuspeca(statusPeca);

            pecaRepository.save(pecaBanco);

            return true;
        }

        return false;
    }


    private boolean podeGerenciarPeca(Peca peca) {

        if (contexto.isAdminMecanica()) {
            return contexto.podeAcessar(peca.getMecanica());
        }

        return peca.getUsuarioCadastro() != null &&
                peca.getUsuarioCadastro().getId()
                        .equals(contexto.getUsuarioLogadoId());
    }
}
