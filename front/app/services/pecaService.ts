import { Peca } from "../types/peca";
import api from "./api";

type PecaApi = Partial<Peca> & { statusPeca?: string; Marca?: string; Descricao?: string };

function normalizarPeca(dado: PecaApi): Peca {
  return new Peca(dado.id ?? null, dado.nome ?? "", dado.preco ?? "", dado.marca ?? dado.Marca ?? "", dado.descricao ?? dado.Descricao ?? "", dado.quantidade ?? "", dado.statuspeca ?? dado.statusPeca ?? "DISPONIVEL");
}

function corpoPeca(peca: Peca) {
  return { nome: peca.nome, preco: peca.preco, Marca: peca.marca, quantidade: peca.quantidade, Descricao: peca.descricao };
}

export async function buscarListaPeca(): Promise<Peca[]> {
  return (await api.get<PecaApi[]>("/pecas")).data.map(normalizarPeca);
}
export async function deletarPeca(id: number): Promise<number> {
  return (await api.delete(`/pecas/${id}/excluir`)).status;
}
export async function atualizarStatusPecas(peca: Peca): Promise<void> {
  const statusPeca = peca.statuspeca === "DISPONIVEL" ? "ESGOTADO" : "DISPONIVEL";
  await api.patch(`/pecas/${peca.id}/status`, { statusPeca });
}
export async function consultaPecaPorId(id: number): Promise<Peca> {
  return normalizarPeca((await api.get<PecaApi>(`/pecas/${id}`)).data);
}
export async function atualizarPeca(peca: Peca): Promise<Peca> {
  return normalizarPeca((await api.put<PecaApi>(`/pecas/${peca.id}`, corpoPeca(peca))).data);
}
export async function salvarPeca(peca: Peca): Promise<Peca> {
  return normalizarPeca((await api.post<PecaApi>("/pecas", corpoPeca(peca))).data);
}
