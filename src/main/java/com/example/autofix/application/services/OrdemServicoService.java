
package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.OrdemServicoRequest;
import com.example.autofix.application.DTOs.OrdemServicoResponse;
import com.example.autofix.application.DTOs.EnumStatusOrdemServico;
import com.example.autofix.domain.entities.Mecanica;
import com.example.autofix.domain.entities.OrdemServico;
import com.example.autofix.domain.repository.OrdemServicoRepository;
import com.example.autofix.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContextoSegurancaService contexto;


    public List<OrdemServicoResponse> listarTodasOrdensServicoTable() {

        List<OrdemServico> ordens;

        if (contexto.isAdmin()) {

            ordens = ordemServicoRepository.findAll();

        } else if (contexto.isAdminMecanica()) {

            Long mecanicaId = contexto.getMecanicaId();

            if (mecanicaId == null) {
                return List.of();
            }

            ordens = ordemServicoRepository.findByMecanica_Id(mecanicaId);

        } else {

            Long usuarioId = contexto.getUsuarioLogadoId();

            if (usuarioId == null) {
                return List.of();
            }

            ordens = ordemServicoRepository.findByUsuarioCadastro_Id(usuarioId);
        }

        return ordens.stream()
                .map(OrdemServicoResponse::new)
                .toList();
    }


    public OrdemServicoResponse buscarPorId(Long id) {

        var ordemServico = ordemServicoRepository.findById(id).orElse(null);

        if (ordemServico == null) {
            return null;
        }

        if (contexto.isAdmin()) {
            return new OrdemServicoResponse(ordemServico);
        }

        if (contexto.isAdminMecanica() &&
                contexto.podeAcessar(ordemServico.getMecanica())) {
            return new OrdemServicoResponse(ordemServico);
        }

        if (ordemServico.getUsuarioCadastro() != null &&
                ordemServico.getUsuarioCadastro().getId()
                        .equals(contexto.getUsuarioLogadoId())) {
            return new OrdemServicoResponse(ordemServico);
        }

        return null;
    }


    public OrdemServicoResponse salvarOrdemServico(
            OrdemServicoRequest ordemServico, Long id) {

        if (id == 0L) {

            Mecanica mecanica = contexto.getMecanicaDoLogado();

            if (mecanica == null || contexto.isAdmin()) {
                throw new RuntimeException(
                        "Usuário não possui permissão para cadastrar ordem de serviço!"
                );
            }

            OrdemServico ordemServicoBanco = new OrdemServico(ordemServico);

            ordemServicoBanco.setMecanica(mecanica);

            ordemServicoBanco.setUsuarioCadastro(
                    usuarioRepository.getReferenceById(
                            contexto.getUsuarioLogadoId()
                    )
            );

            ordemServicoRepository.save(ordemServicoBanco);

            return new OrdemServicoResponse(ordemServicoBanco);

        } else {

            OrdemServico ordemServicoBanco =
                    ordemServicoRepository.findById(id).orElse(null);

            if (ordemServicoBanco != null &&
                    !contexto.isAdmin() &&
                    podeGerenciarOrdem(ordemServicoBanco)) {

                ordemServicoBanco.setDataAbertura(ordemServico.dataAbertura());
                ordemServicoBanco.setDataConclusao(ordemServico.dataConclusao());
                ordemServicoBanco.setDescricao(ordemServico.descricao());

                ordemServicoRepository.save(ordemServicoBanco);

                return new OrdemServicoResponse(ordemServicoBanco);
            }
        }

        return null;
    }


    public boolean atualizarStatus(
            EnumStatusOrdemServico statusOrdemServico, Long id) {

        OrdemServico ordemServicoBanco =
                ordemServicoRepository.findById(id).orElse(null);

        if (ordemServicoBanco != null &&
                !contexto.isAdmin() &&
                podeGerenciarOrdem(ordemServicoBanco)) {

            ordemServicoBanco.setStatusOrdemServico(statusOrdemServico);

            ordemServicoRepository.save(ordemServicoBanco);

            return true;
        }

        return false;
    }


    private boolean podeGerenciarOrdem(OrdemServico ordemServico) {

        if (contexto.isAdminMecanica()) {
            return contexto.podeAcessar(ordemServico.getMecanica());
        }

        return ordemServico.getUsuarioCadastro() != null &&
                ordemServico.getUsuarioCadastro().getId()
                        .equals(contexto.getUsuarioLogadoId());
    }
}
