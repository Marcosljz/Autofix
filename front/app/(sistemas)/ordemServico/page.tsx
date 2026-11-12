"use client";

import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { atualizarStatusOrdemServico, buscarListaOrdemServico, deletarOrdemServico } from "@/app/services/ordemServicoService";
import { OrdemServico } from "@/app/types/ordemservico";
import type { AppDispatch, RootState } from "@/app/redux/store";
import { definirOrdensServico, removerOrdemServico } from "@/app/redux/slices/ordemServicoSlice";
import Link from "next/link";

export default function OrdensServico() {
  const dispatch = useDispatch<AppDispatch>();
  const ordensServico = useSelector((state: RootState) => state.ordensServico.lista);

  useEffect(() => { carregarDados(); }, []);

  const carregarDados = async () => {
    try {
      const dados = await buscarListaOrdemServico();
      dispatch(definirOrdensServico(dados));
    } catch {
      alert("Erro ao carregar dados do servidor!");
    }
  };

  const handleDeletarOrdemServico = async (ordemServico: OrdemServico) => {
    if (!ordemServico.id || !window.confirm("Deseja realmente excluir esta ordem de serviço?")) return;
    try {
      await deletarOrdemServico(ordemServico.id);
      dispatch(removerOrdemServico(ordemServico.id));
      alert("Ordem de serviço excluída com sucesso.");
    } catch {
      alert("Não foi possível excluir a ordem de serviço.");
    }
  };

  const handleAlterarStatusOrdemServico = async (ordemServico: OrdemServico) => {
    try {
      await atualizarStatusOrdemServico(ordemServico);
      await carregarDados();
    } catch {
      alert("Não foi possível atualizar o status. Verifique o estoque das peças.");
    }
  };

  const formatarData = (valor: Date | string | null) => {
    if (!valor) return "-";
    const data = new Date(valor);
    return Number.isNaN(data.getTime()) ? "-" : data.toLocaleString("pt-BR");
  };

  return (
    <div className="min-h-screen bg-black px-6 py-10">
      <div className="max-w-5xl mx-auto flex items-center justify-between mb-8">
        <h1 className="text-2xl font-bold text-white">Ordens de Serviço</h1>
        <Link href="/ordemServico/novo" className="inline-flex items-center justify-center px-5 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-xl">Nova ordem de serviço</Link>
      </div>
      <div className="max-w-5xl mx-auto">
        <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-x-auto">
          <table className="w-full text-left">
            <thead className="bg-neutral-800/50">
              <tr>{["ID", "Veículo", "Data Abertura", "Data Conclusão", "Status", "Descrição", "Ações"].map((titulo) => <th key={titulo} className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">{titulo}</th>)}</tr>
            </thead>
            <tbody className="divide-y divide-slate-800 text-slate-200">
              {ordensServico.map((ordem) => (
                <tr key={ordem.id} className="hover:bg-slate-800/40">
                  <td className="px-6 py-4 text-sm text-white">{ordem.id}</td>
                  <td className="px-6 py-4 text-sm text-white">{ordem.veiculoId ?? "-"}</td>
                  <td className="px-6 py-4 text-sm text-white">{formatarData(ordem.dataAbertura)}</td>
                  <td className="px-6 py-4 text-sm text-white">{formatarData(ordem.dataConclusao)}</td>
                  <td className="px-6 py-4 text-sm text-white">{ordem.statusOrdemServico}</td>
                  <td className="px-6 py-4 text-sm text-white">{ordem.descricao}</td>
                  <td className="px-6 py-4 text-sm">
                    <div className="flex items-center gap-2">
                      <Link href={`/ordemServico/${ordem.id}/editar`} className="text-blue-400 hover:text-blue-300">Editar</Link>
                      <button type="button" onClick={() => handleDeletarOrdemServico(ordem)} className="text-orange-400 hover:text-orange-300">Excluir</button>
                      {ordem.statusOrdemServico !== "EXCLUIDO" && <button type="button" onClick={() => handleAlterarStatusOrdemServico(ordem)} className="text-green-400 hover:text-green-300">{ordem.statusOrdemServico === "ABERTO" ? "Iniciar" : ordem.statusOrdemServico === "ANDAMENTO" ? "Finalizar" : "Reabrir"}</button>}
                    </div>
                  </td>
                </tr>
              ))}
              {ordensServico.length === 0 && <tr><td colSpan={7} className="px-6 py-12 text-center text-slate-100">Nenhuma ordem de serviço encontrada.</td></tr>}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
