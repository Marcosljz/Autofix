
package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.OrdemServicoPecaRequest;
import com.example.autofix.application.DTOs.PecaOrdemServicoResponse;
import com.example.autofix.application.DTOs.EnumStatusOrdemServico;
import com.example.autofix.application.DTOs.EnumStatusPeca;
import com.example.autofix.domain.entities.OrdemServico;
import com.example.autofix.domain.entities.OrdemServicoPeca;
import com.example.autofix.domain.entities.Peca;
import com.example.autofix.domain.repository.OrdemServicoPecaRepository;
import com.example.autofix.domain.repository.OrdemServicoRepository;
import com.example.autofix.domain.repository.PecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdemServicoPecaService {

    @Autowired
    private OrdemServicoPecaRepository repository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private ContextoSegurancaService contexto;


    public PecaOrdemServicoResponse salvar(OrdemServicoPecaRequest request) {

        // ADMIN possui somente permissão de visualização
        if (contexto.isAdmin()) {
            throw new RuntimeException(
                    "Seu perfil de administrador permite apenas visualizar os registros."
            );
        }

        OrdemServico ordem = ordemServicoRepository
                .findById(request.ordemServicoId()).orElse(null);

        Peca peca = pecaRepository
                .findById(request.pecaId()).orElse(null);

        if (ordem == null || peca == null) {
            throw new RuntimeException(
                    "Ordem de serviço ou peça não encontrada!"
            );
        }

        // Verifica se a ordem e a peça pertencem à mecânica do usuário
        if (!contexto.podeAcessar(ordem.getMecanica())
                || !contexto.podeAcessar(peca.getMecanica())) {

            throw new RuntimeException(
                    "Sem permissão para vincular esta peça!"
            );
        }

        // ROLE_USER só pode adicionar peças nas próprias ordens
        if (!contexto.isAdminMecanica()) {

            Long usuarioId = contexto.getUsuarioLogadoId();

            if (ordem.getUsuarioCadastro() == null
                    || usuarioId == null
                    || !ordem.getUsuarioCadastro().getId().equals(usuarioId)) {

                throw new RuntimeException(
                        "Você só pode adicionar peças nas ordens de serviço que cadastrou!"
                );
            }
        }

        // Impede alteração de peças em OS finalizada
        if (ordem.getStatusOrdemServico() == EnumStatusOrdemServico.FECHADO
                || ordem.isEstoqueBaixado()) {

            throw new RuntimeException(
                    "Não é possível alterar peças de uma OS finalizada!"
            );
        }

        // Verifica se a peça está disponível
        if (peca.getStatuspeca() != EnumStatusPeca.DISPONIVEL) {

            throw new RuntimeException(
                    "Esta peça não está disponível para utilização!"
            );
        }

        // Verifica se a quantidade informada é válida
        if (request.quantidade() == null || request.quantidade() <= 0) {

            throw new RuntimeException(
                    "Informe uma quantidade válida!"
            );
        }

        // Verifica se a quantidade do estoque é válida
        if (peca.getQuantidade() == null) {
            throw new RuntimeException("Quantidade de estoque não informada!");
        }

        Integer estoqueDisponivel;

        try {
            estoqueDisponivel = Integer.parseInt(peca.getQuantidade());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Quantidade de estoque inválida!");
        }

        // Soma a quantidade da mesma peça que já foi adicionada nesta OS
        int quantidadeJaAdicionada = repository
                .findByOrdemServico_Id(request.ordemServicoId())
                .stream()
                .filter(item -> item.getPeca() != null
                        && item.getPeca().getId().equals(peca.getId()))
                .mapToInt(OrdemServicoPeca::getQuantidade)
                .sum();

        // Verifica o estoque considerando as peças já adicionadas
        if (quantidadeJaAdicionada + request.quantidade() > estoqueDisponivel) {

            throw new RuntimeException(
                    "Estoque insuficiente! Disponível: "
                            + (estoqueDisponivel - quantidadeJaAdicionada)
            );
        }

        OrdemServicoPeca item = new OrdemServicoPeca();

        item.setOrdemServico(ordem);
        item.setPeca(peca);
        item.setQuantidade(request.quantidade());

        OrdemServicoPeca salvo = repository.save(item);

        return new PecaOrdemServicoResponse(
                salvo.getId(),
                ordem.getId(),
                peca.getId(),
                salvo.getQuantidade()
        );
    }
}
