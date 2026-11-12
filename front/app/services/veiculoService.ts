import { veiculo } from "../types/veiculo";
import api from "./api";

type VeiculoApi = Partial<veiculo> & { Modelo?: string };

function normalizarVeiculo(dado: VeiculoApi): veiculo {
  return new veiculo(dado.id ?? null, dado.placa ?? "", dado.modelo ?? dado.Modelo ?? "", dado.ano ?? "", dado.cor ?? "", Number(dado.quilometragem ?? 0), dado.statusVeiculo ?? "ATIVO");
}

function corpoVeiculo(dado: veiculo) {
  return { placa: dado.placa, Modelo: dado.modelo, ano: dado.ano, cor: dado.cor, quilometragem: dado.quilometragem };
}

export async function buscarListaVeiculos(): Promise<veiculo[]> {
  const resposta = await api.get<VeiculoApi[]>("/veiculos");
  return resposta.data.map(normalizarVeiculo);
}
export async function deletarVeiculo(id: number): Promise<number> {
  return (await api.delete(`/veiculos/${id}/excluir`)).status;
}
export async function atualizarStatusVeiculo(dadosVeiculo: veiculo): Promise<void> {
  const statusVeiculo = dadosVeiculo.statusVeiculo === "ATIVO" ? "INATIVO" : "ATIVO";
  await api.patch(`/veiculos/${dadosVeiculo.id}/status`, { statusVeiculo });
}
export async function consultaVeiculoPorId(id: number): Promise<veiculo> {
  return normalizarVeiculo((await api.get<VeiculoApi>(`/veiculos/${id}`)).data);
}
export async function atualizarVeiculo(dadosVeiculo: veiculo): Promise<veiculo> {
  return normalizarVeiculo((await api.put<VeiculoApi>(`/veiculos/${dadosVeiculo.id}`, corpoVeiculo(dadosVeiculo))).data);
}
export async function salvarVeiculo(dadosVeiculo: veiculo): Promise<veiculo> {
  return normalizarVeiculo((await api.post<VeiculoApi>("/veiculos", corpoVeiculo(dadosVeiculo))).data);
}
