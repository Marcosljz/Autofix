import { OrdemServico, OrdemServicoPecaRequest } from "../types/ordemservico";
import api from "./api";

export async function buscarListaOrdemServico(): Promise<OrdemServico[]> {
  const resposta = await api.get<OrdemServico[]>("/ordemservicos");
  return resposta.data;
}

export async function deletarOrdemServico(id: number): Promise<number> {
  const resposta = await api.delete(`/ordemservicos/${id}/excluir`);
  return resposta.status;
}

export async function atualizarStatusOrdemServico(ordemServico: OrdemServico): Promise<void> {
  let statusOrdemServico: string;
  if (ordemServico.statusOrdemServico === "ABERTO") statusOrdemServico = "ANDAMENTO";
  else if (ordemServico.statusOrdemServico === "ANDAMENTO") statusOrdemServico = "FECHADO";
  else statusOrdemServico = "ABERTO";
  await api.patch(`/ordemservicos/${ordemServico.id}/status`, { statusOrdemServico });
}

export async function consultaOrdemServicoPorId(id: number): Promise<OrdemServico> {
  const resposta = await api.get<OrdemServico>(`/ordemservicos/${id}`);
  return resposta.data;
}

export async function atualizarOrdemServico(ordemServico: OrdemServico): Promise<OrdemServico> {
  const resposta = await api.put<OrdemServico>(`/ordemservicos/${ordemServico.id}`, {
    dataAbertura: ordemServico.dataAbertura,
    dataConclusao: ordemServico.dataConclusao,
    descricao: ordemServico.descricao,
    veiculoId: ordemServico.veiculoId,
  });
  return resposta.data;
}

export async function salvarOrdemServico(ordemServico: OrdemServico): Promise<OrdemServico> {
  const resposta = await api.post<OrdemServico>("/ordemservicos", {
    dataAbertura: ordemServico.dataAbertura,
    dataConclusao: ordemServico.dataConclusao,
    descricao: ordemServico.descricao,
    veiculoId: ordemServico.veiculoId,
  });
  return resposta.data;
}

export async function adicionarPecaNaOrdemServico(request: OrdemServicoPecaRequest): Promise<void> {
  await api.post("/ordens-servico-pecas", request);
}
