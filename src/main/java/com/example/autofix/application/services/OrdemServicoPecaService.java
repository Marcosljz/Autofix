
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

import java.util.List;

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


    // Lista as peças vinculadas a uma ordem de serviço
    public List<PecaOrdemServicoResponse> listarPorOrdemServico(Long ordemServicoId) {

        OrdemServico ordem = ordemServicoRepository
                .findById(ordemServicoId).orElse(null);

        if (ordem == null) {
            throw new RuntimeException("Ordem de serviço não encontrada!");
        }

        if (!contexto.podeAcessar(ordem.getMecanica())) {
            throw new RuntimeException(
                    "Sem permissão para visualizar esta ordem!"
            );
        }

        if (!contexto.isAdmin() && !contexto.isAdminMecanica()) {

            Long usuarioId = contexto.getUsuarioLogadoId();

            if (ordem.getUsuarioCadastro() == null
                    || usuarioId == null
                    || !ordem.getUsuarioCadastro().getId().equals(usuarioId)) {

                throw new RuntimeException(
                        "Você só pode visualizar as ordens de serviço que cadastrou!"
                );
            }
        }

        return repository.findByOrdemServico_Id(ordemServicoId)
                .stream()
                .map(item -> new PecaOrdemServicoResponse(
                        item.getId(),
                        ordem.getId(),
                        item.getPeca().getId(),
                        item.getQuantidade()
                ))
                .toList();
    }


    // Atualiza a quantidade de uma peça vinculada
    public PecaOrdemServicoResponse atualizar(
            Long id,
            OrdemServicoPecaRequest request) {

        OrdemServicoPeca item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Peça vinculada à ordem não encontrada!"
                ));

        OrdemServico ordem = item.getOrdemServico();
        Peca peca = item.getPeca();

        validarPermissaoAlteracao(ordem, peca);

        if (!ordem.getId().equals(request.ordemServicoId())
                || !peca.getId().equals(request.pecaId())) {

            throw new RuntimeException(
                    "Não é permitido alterar a ordem ou a peça vinculada!"
            );
        }

        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new RuntimeException("Informe uma quantidade válida!");
        }

        if (peca.getStatuspeca() != EnumStatusPeca.DISPONIVEL) {
            throw new RuntimeException(
                    "Esta peça não está disponível para utilização!"
            );
        }

        if (peca.getQuantidade() == null) {
            throw new RuntimeException("Quantidade de estoque não informada!");
        }

        Integer estoqueDisponivel;

        try {
            estoqueDisponivel = Integer.parseInt(peca.getQuantidade());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Quantidade de estoque inválida!");
        }

        // Desconsidera o próprio registro ao calcular o estoque utilizado
        int quantidadeJaAdicionada = repository
                .findByOrdemServico_Id(ordem.getId())
                .stream()
                .filter(outroItem -> outroItem.getPeca() != null
                        && outroItem.getPeca().getId().equals(peca.getId())
                        && !outroItem.getId().equals(id))
                .mapToInt(OrdemServicoPeca::getQuantidade)
                .sum();

        if (quantidadeJaAdicionada + request.quantidade() > estoqueDisponivel) {
            throw new RuntimeException(
                    "Estoque insuficiente! Disponível: "
                            + (estoqueDisponivel - quantidadeJaAdicionada)
            );
        }

        item.setQuantidade(request.quantidade());

        OrdemServicoPeca atualizado = repository.save(item);

        return new PecaOrdemServicoResponse(
                atualizado.getId(),
                ordem.getId(),
                peca.getId(),
                atualizado.getQuantidade()
        );
    }


    // Exclui uma peça vinculada à ordem
    public void excluir(Long id) {

        OrdemServicoPeca item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Peça vinculada à ordem não encontrada!"
                ));

        validarPermissaoAlteracao(
                item.getOrdemServico(),
                item.getPeca()
        );

        repository.delete(item);
    }


    // Valida as permissões e o status da ordem
    private void validarPermissaoAlteracao(OrdemServico ordem, Peca peca) {

        if (contexto.isAdmin()) {
            throw new RuntimeException(
                    "Seu perfil de administrador permite apenas visualizar os registros."
            );
        }

        if (!contexto.podeAcessar(ordem.getMecanica())
                || !contexto.podeAcessar(peca.getMecanica())) {

            throw new RuntimeException(
                    "Sem permissão para alterar esta peça!"
            );
        }

        if (!contexto.isAdminMecanica()) {

            Long usuarioId = contexto.getUsuarioLogadoId();

            if (ordem.getUsuarioCadastro() == null
                    || usuarioId == null
                    || !ordem.getUsuarioCadastro().getId().equals(usuarioId)) {

                throw new RuntimeException(
                        "Você só pode alterar peças das ordens de serviço que cadastrou!"
                );
            }
        }

        if (ordem.getStatusOrdemServico() == EnumStatusOrdemServico.FECHADO
                || ordem.isEstoqueBaixado()) {

            throw new RuntimeException(
                    "Não é possível alterar peças de uma OS finalizada!"
            );
        }
    }
}
