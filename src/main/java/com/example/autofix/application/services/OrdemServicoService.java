
package com.example.autofix.application.services;

import com.example.autofix.application.DTOs.OrdemServicoRequest;
import com.example.autofix.application.DTOs.OrdemServicoResponse;
import com.example.autofix.application.DTOs.EnumStatusOrdemServico;
import com.example.autofix.application.DTOs.EnumStatusPeca;
import com.example.autofix.domain.entities.Mecanica;
import com.example.autofix.domain.entities.OrdemServico;
import com.example.autofix.domain.entities.Peca;
import com.example.autofix.domain.entities.OrdemServicoPeca;
import com.example.autofix.domain.entities.Veiculo;
import com.example.autofix.domain.repository.OrdemServicoRepository;
import com.example.autofix.domain.repository.UsuarioRepository;
import com.example.autofix.domain.repository.OrdemServicoPecaRepository;
import com.example.autofix.domain.repository.VeiculoRepository;
import com.example.autofix.domain.repository.PecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContextoSegurancaService contexto;

    @Autowired
    private OrdemServicoPecaRepository ordemServicoPecaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private PecaRepository pecaRepository;


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


    // Soma as quantidades de peças repetidas na mesma OS
    private Map<Long, Integer> somarQuantidadesPorPeca(
            List<OrdemServicoPeca> itens) {

        Map<Long, Integer> quantidades = new HashMap<>();

        for (OrdemServicoPeca item : itens) {

            if (item.getPeca() == null
                    || item.getQuantidade() == null
                    || item.getQuantidade() <= 0) {

                throw new RuntimeException(
                        "Existe uma peça ou quantidade inválida na ordem de serviço!"
                );
            }

            Long pecaId = item.getPeca().getId();

            quantidades.merge(
                    pecaId,
                    item.getQuantidade(),
                    Integer::sum
            );
        }

        return quantidades;
    }


    // Organiza as peças pelo ID
    private Map<Long, Peca> mapearPecas(List<OrdemServicoPeca> itens) {

        Map<Long, Peca> pecas = new HashMap<>();

        for (OrdemServicoPeca item : itens) {

            Peca peca = item.getPeca();

            pecas.put(peca.getId(), peca);
        }

        return pecas;
    }


    // Verifica se existe estoque suficiente para finalizar a OS
    private boolean verificarEstoqueDisponivel(Long ordemServicoId) {

        List<OrdemServicoPeca> pecasUtilizadas =
                ordemServicoPecaRepository.findByOrdemServico_Id(ordemServicoId);

        try {

            Map<Long, Integer> quantidades =
                    somarQuantidadesPorPeca(pecasUtilizadas);

            Map<Long, Peca> pecas = mapearPecas(pecasUtilizadas);

            for (Long pecaId : quantidades.keySet()) {

                Peca peca = pecas.get(pecaId);

                if (peca.getStatuspeca() != EnumStatusPeca.DISPONIVEL
                        || peca.getQuantidade() == null) {

                    return false;
                }

                int estoque = Integer.parseInt(peca.getQuantidade());

                if (estoque < quantidades.get(pecaId)) {
                    return false;
                }
            }

            return true;

        } catch (NumberFormatException e) {

            return false;
        }
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


    // Realiza a baixa do estoque ao finalizar a OS
    private void baixarEstoque(Long ordemServicoId) {

        List<OrdemServicoPeca> pecasUtilizadas =
                ordemServicoPecaRepository.findByOrdemServico_Id(ordemServicoId);

        Map<Long, Integer> quantidades =
                somarQuantidadesPorPeca(pecasUtilizadas);

        Map<Long, Peca> pecas = mapearPecas(pecasUtilizadas);

        for (Long pecaId : quantidades.keySet()) {

            Peca peca = pecas.get(pecaId);

            int estoque = Integer.parseInt(peca.getQuantidade());

            int novoEstoque = estoque - quantidades.get(pecaId);

            peca.setQuantidade(String.valueOf(novoEstoque));

            if (novoEstoque == 0) {

                peca.setStatuspeca(EnumStatusPeca.ESGOTADO);

            } else {

                peca.setStatuspeca(EnumStatusPeca.DISPONIVEL);
            }

            pecaRepository.save(peca);
        }
    }


    // Devolve as peças ao estoque ao reabrir a OS
    private void devolverEstoque(Long ordemServicoId) {

        List<OrdemServicoPeca> pecasUtilizadas =
                ordemServicoPecaRepository.findByOrdemServico_Id(ordemServicoId);

        Map<Long, Integer> quantidades =
                somarQuantidadesPorPeca(pecasUtilizadas);

        Map<Long, Peca> pecas = mapearPecas(pecasUtilizadas);

        for (Long pecaId : quantidades.keySet()) {

            Peca peca = pecas.get(pecaId);

            int estoque = Integer.parseInt(peca.getQuantidade());

            int novoEstoque = estoque + quantidades.get(pecaId);

            peca.setQuantidade(String.valueOf(novoEstoque));

            // Não altera o status de uma peça excluída
            if (peca.getStatuspeca() != EnumStatusPeca.EXCLUIDO) {

                peca.setStatuspeca(EnumStatusPeca.DISPONIVEL);
            }

            pecaRepository.save(peca);
        }
    }


    private Veiculo buscarVeiculoDaOrdem(Long veiculoId, Mecanica mecanica) {

        if (veiculoId == null) {
            throw new RuntimeException("Informe o veículo da ordem de serviço!");
        }

        Veiculo veiculo = veiculoRepository.findById(veiculoId).orElse(null);

        if (veiculo == null) {
            throw new RuntimeException("Veículo não encontrado!");
        }

        if (veiculo.getMecanica() == null ||
                !mecanica.getId().equals(veiculo.getMecanica().getId())) {

            throw new RuntimeException(
                    "Este veículo não pertence à mecânica da ordem de serviço!"
            );
        }

        return veiculo;
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

            Veiculo veiculo = buscarVeiculoDaOrdem(
                    ordemServico.veiculoId(), mecanica
            );

            OrdemServico ordemServicoBanco = new OrdemServico(ordemServico);

            ordemServicoBanco.setMecanica(mecanica);

            ordemServicoBanco.setUsuarioCadastro(
                    usuarioRepository.getReferenceById(
                            contexto.getUsuarioLogadoId()
                    )
            );

            ordemServicoBanco.setVeiculo(veiculo);

            ordemServicoRepository.save(ordemServicoBanco);

            return new OrdemServicoResponse(ordemServicoBanco);

        } else {

            OrdemServico ordemServicoBanco =
                    ordemServicoRepository.findById(id).orElse(null);

            if (ordemServicoBanco != null &&
                    !contexto.isAdmin() &&
                    podeGerenciarOrdem(ordemServicoBanco)) {

                Veiculo veiculo = buscarVeiculoDaOrdem(
                        ordemServico.veiculoId(),
                        ordemServicoBanco.getMecanica()
                );

                ordemServicoBanco.setDataAbertura(ordemServico.dataAbertura());
                ordemServicoBanco.setDataConclusao(ordemServico.dataConclusao());
                ordemServicoBanco.setDescricao(ordemServico.descricao());
                ordemServicoBanco.setVeiculo(veiculo);

                ordemServicoRepository.save(ordemServicoBanco);

                return new OrdemServicoResponse(ordemServicoBanco);
            }
        }

        return null;
    }


    @Transactional
    public boolean atualizarStatus(
            EnumStatusOrdemServico statusOrdemServico, Long id) {

        OrdemServico ordemServicoBanco =
                ordemServicoRepository.findById(id).orElse(null);

        if (ordemServicoBanco != null &&
                !contexto.isAdmin() &&
                podeGerenciarOrdem(ordemServicoBanco)) {

            // Finalizando a OS
            if (statusOrdemServico == EnumStatusOrdemServico.FECHADO
                    && !ordemServicoBanco.isEstoqueBaixado()) {

                if (!verificarEstoqueDisponivel(id)) {

                    throw new RuntimeException(
                            "Não é possível finalizar a ordem de serviço! "
                                    + "Verifique o status e a quantidade das peças em estoque."
                    );
                }

                baixarEstoque(id);

                ordemServicoBanco.setEstoqueBaixado(true);
            }

            // Reabrindo ou cancelando uma OS que já teve o estoque baixado
            if (statusOrdemServico != EnumStatusOrdemServico.FECHADO
                    && ordemServicoBanco.isEstoqueBaixado()) {

                devolverEstoque(id);

                ordemServicoBanco.setEstoqueBaixado(false);
            }

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
