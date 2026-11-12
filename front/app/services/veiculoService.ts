import { veiculo } from "../types/veiculo";
import api from "./api";

export async function buscarListaVeiculos(): Promise<veiculo[]> {
  const resposta = await api.get<veiculo[]>("/veiculos");
  return resposta.data;
}

export async function deletarVeiculo(id: number): Promise<number> {
  const resposta = await api.delete(`/veiculos/${id}/excluir`);
  return resposta.status;
}

export async function atualizarStatusVeiculo(dadosVeiculo: veiculo): Promise<void> {
  const statusVeiculo = dadosVeiculo.statusVeiculo === "ATIVO" ? "INATIVO" : "ATIVO";

  await api.patch(`/veiculos/${dadosVeiculo.id}/status`, { statusVeiculo });
}

export async function consultaVeiculoPorId(id: number): Promise<veiculo> {
  const resposta = await api.get<veiculo>(`/veiculos/${id}`);
  return resposta.data;
}

export async function atualizarVeiculo(dadosVeiculo: veiculo): Promise<veiculo> {
  const resposta = await api.put<veiculo>(`/veiculos/${dadosVeiculo.id}`, dadosVeiculo);
  return resposta.data;
}

export async function salvarVeiculo(dadosVeiculo: veiculo): Promise<veiculo> {
  const resposta = await api.post<veiculo>("/veiculos", dadosVeiculo);
  return resposta.data;
}
