import { Peca } from "../types/peca";
import api from "./api";

export async function buscarListaPeca(): Promise<Peca[]> {
  const resposta = await api.get<Peca[]>("/pecas");
  return resposta.data;
}

export async function deletarPeca(id: number): Promise<number> {
  const resposta = await api.delete(`/pecas/${id}/excluir`);
  return resposta.status;
}

export async function atualizarStatusPecas(peca: Peca): Promise<void> {
  const statusPeca = peca.statuspeca === "DISPONIVEL" ? "ESGOTADO" : "DISPONIVEL";
  await api.patch(`/pecas/${peca.id}/status`, { statusPeca });
}

export async function consultaPecaPorId(id: number): Promise<Peca> {
  const resposta = await api.get<Peca>(`/pecas/${id}`);
  return resposta.data;
}

export async function atualizarPeca(peca: Peca): Promise<Peca> {
  const resposta = await api.put<Peca>(`/pecas/${peca.id}`, peca);
  return resposta.data;
}

export async function salvarPeca(peca: Peca): Promise<Peca> {
  const resposta = await api.post<Peca>("/pecas", peca);
  return resposta.data;
}
