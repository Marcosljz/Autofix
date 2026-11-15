
import { OrdemServico } from "../types/ordemservico";
import api from "./api";

export async function buscarListaOrdemServico(): Promise<OrdemServico[]> {
    const dados = await api.get<OrdemServico[]>(
        "http://localhost:8080/ordemservicos"
    );

    if (dados.status == 200) {
        return dados.data;
    }

    return [];
}

export async function deletarOrdemServico(id: number): Promise<number> {
    var result = api.delete(
        "http://localhost:8080/ordemservicos/" + id + "/excluir"
    );

    return (await result).status;
}


export async function atualizarStatusOrdemServico(
    ordemservico: OrdemServico
): Promise<void> {
    let novoStatus = {};

    if (ordemservico.statusOrdemServico === "ABERTO") {
        novoStatus = { statusOrdemServico: "ANDAMENTO" };
    } else if (ordemservico.statusOrdemServico === "ANDAMENTO") {
        novoStatus = { statusOrdemServico: "FECHADO" };
    } else if (ordemservico.statusOrdemServico === "FECHADO") {
        novoStatus = { statusOrdemServico: "ABERTO" };
    } else if (ordemservico.statusOrdemServico === "EXCLUIDO") {
        novoStatus = { statusOrdemServico: "ABERTO" };
    }

    try {
        const dadosRetorno = await api.patch(
            "http://localhost:8080/ordemservicos/" +
            ordemservico.id +
            "/status",
            novoStatus
        );

        if (dadosRetorno.status === 200) {
            alert("Status atualizado com sucesso!");
        }

    } catch (erro: any) {
        const mensagem = erro.response?.data;

        alert(
            typeof mensagem === "string"
                ? mensagem
                : "Não foi possível atualizar o status da ordem de serviço."
        );

        throw erro;
    }
}

export async function consultaOrdemServicoPorId(
    id: number
): Promise<OrdemServico> {
    const valorOrdemServicoBack = await api.get<OrdemServico>(
        "http://localhost:8080/ordemservicos/" + id
    );

    return valorOrdemServicoBack.data;
}

export async function atualizarOrdemServico(
    ordemservico: OrdemServico
): Promise<number> {
    var dadosRetorno = await api.put<number>(
        "http://localhost:8080/ordemservicos/" + ordemservico.id,
        ordemservico
    );

    return dadosRetorno.data;
}

export async function salvarOrdemServico(
    ordemservico: OrdemServico
): Promise<OrdemServico> {
    const dadosRetorno = await api.post<OrdemServico>(
        "http://localhost:8080/ordemservicos",
        ordemservico
    );

    return dadosRetorno.data;
}

export async function associarPecaOrdemServico(
    ordemServicoId: number,
    pecaId: number,
    quantidade: number
): Promise<void> {
    await api.post("/ordens-servico-pecas", {
        ordemServicoId,
        pecaId,
        quantidade
    });
}

export interface PecaOrdemServico {
    id: number;
    ordemServicoId: number;
    pecaId: number;
    quantidade: number;
}

export async function listarPecasPorOrdemServico(
    ordemServicoId: number
): Promise<PecaOrdemServico[]> {

    const resposta = await api.get<PecaOrdemServico[]>(
        `/ordens-servico-pecas/${ordemServicoId}`
    );

    return resposta.data;
}

export async function atualizarPecaOrdemServico(
    id: number,
    ordemServicoId: number,
    pecaId: number,
    quantidade: number
): Promise<void> {

    await api.put(`/ordens-servico-pecas/${id}`, {
        ordemServicoId,
        pecaId,
        quantidade
    });
}

export async function excluirPecaOrdemServico(id: number): Promise<void> {

    await api.delete(`/ordens-servico-pecas/${id}`);
}

