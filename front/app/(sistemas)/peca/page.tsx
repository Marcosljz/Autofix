"use client"

import { Peca } from "@/app/types/peca";
import axios from "axios";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Pecas() {

    const [pecas, setPecas] = useState<Peca[]>([]);

    useEffect(() => {
        carregarDados();
    }, []);

    const carregarDados = async () => {

        try {

            const dados = await axios.get<Peca[]>(
                "http://localhost:8080/pecas"
            );

            setPecas(dados.data);

        } catch (error) {
            alert("Erro ao carregar dados do servidor!")
        }
    }

    const handleDeletarPeca = async (peca: Peca) => {

        var dadosRetorno = await axios.delete(
            'http://localhost:8080/pecas/' + peca.id + '/excluir'
        );

        if (dadosRetorno.status === 200) {
            alert("Excluído com sucesso");
        } else {
            alert(dadosRetorno.data);
            return;
        }

        carregarDados();
    }

    const handleAlterarStatusPeca = async (peca: Peca) => {

        var novoStatus = {};

        if (peca.statuspeca === "DISPONIVEL") {
            novoStatus = { statusPeca: "ESGOTADO" }
        } else {
            novoStatus = { statusPeca: "DISPONIVEL" }
        }

        var dadosRetorno = await axios.patch(
            'http://localhost:8080/pecas/' + peca.id + '/status',
            novoStatus
        );

        if (dadosRetorno.status === 200) {
            alert("Atualizado status com sucesso!");
        } else {
            alert(dadosRetorno.data);
            return;
        }

        carregarDados();
    }

    return (

        <div className="min-h-screen bg-black px-6 py-10">

            <div className="max-w-5xl mx-auto flex items-center justify-between mb-8">

                <h1 className="text-2xl font-bold text-white">
                    Peças
                </h1>

                <Link
                    href="/peca/novo"
                    className="inline-flex items-center justify-center px-5 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-xl transition-colors active:scale-95"
                >
                    Nova peça
                </Link>

            </div>

            <div className="max-w-5xl mx-auto">

                <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-hidden">

                    <table className="w-full text-left">

                        <thead className="bg-neutral-800/50">

                            <tr>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    ID
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Nome
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Preço
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Marca
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Quantidade
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Descrição
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Status
                                </th>

                                <th className="px-4 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide w-[340px]">
                                    Ações
                                </th>

                            </tr>

                        </thead>

                        <tbody className="divide-y divide-slate-800 text-slate-200">

                            {pecas.map((peca) => (

                                <tr
                                    key={peca.id}
                                    className="hover:bg-slate-800/40 transition-colors"
                                >

                                    <td className="px-6 py-4 text-sm text-white">
                                        {peca.id}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {peca.nome}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {peca.preco}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {peca.marca}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {peca.quantidade}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {peca.descricao}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {peca.statuspeca}
                                    </td>

                                    <td className="px-4 py-4 w-[300px]">

                                        <div className="flex items-center justify-end gap-1">

                                            <Link
                                                href={`/peca/${peca.id}/editar`}
                                                className="group inline-flex items-center gap-1 px-2.5 py-2 text-xs font-semibold
                                                text-blue-400 bg-blue-500/10
                                                border border-blue-500/30
                                             hover:text-white hover:bg-blue-500
                                             hover:border-blue-400
                                             rounded-xl
                                             transition-all duration-300
                                             hover:-translate-y-0.5
                                             hover:shadow-lg hover:shadow-blue-500/20"
                                            >
                                                <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    fill="none"
                                                    viewBox="0 0 24 24"
                                                    strokeWidth={2}
                                                    stroke="currentColor"
                                                    className="w-3.5 h-3.5 transition-transform duration-300 group-hover:rotate-[-8deg] group-hover:scale-110"
                                                >
                                                    <path
                                                        strokeLinecap="round"
                                                        strokeLinejoin="round"
                                                        d="M16.862 3.487a2.25 2.25 0 0 1 3.182 3.182L8.25 18.463 4 19.5l1.037-4.25L16.862 3.487Z"
                                                    />
                                                </svg>

                                                Editar
                                            </Link>


                                            <button
                                                type="button"
                                                onClick={() => handleDeletarPeca(peca)}
                                                className="group inline-flex items-center gap-1 px-2.5 py-2 text-xs font-semibold
                                             text-orange-400 bg-orange-500/10
                                             border border-orange-500/30
                                             hover:text-white hover:bg-orange-500
                                             hover:border-orange-400
                                             rounded-xl
                                             transition-all duration-300
                                             hover:-translate-y-0.5
                                             hover:shadow-lg hover:shadow-orange-500/20"
                                            >
                                                <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    fill="none"
                                                    viewBox="0 0 24 24"
                                                    strokeWidth={2}
                                                    stroke="currentColor"
                                                    className="w-3.5 h-3.5 transition-transform duration-300 group-hover:scale-110"
                                                >
                                                    <path
                                                        strokeLinecap="round"
                                                        strokeLinejoin="round"
                                                        d="M6 7h12M9 7V5h6v2m-7 4v6m4-6v6m4-6v6M5 7l1 14h12l1-14"
                                                    />
                                                </svg>

                                                Deletar
                                            </button>


                                            <button
                                                type="button"
                                                onClick={() => handleAlterarStatusPeca(peca)}
                                                className={`group inline-flex items-center gap-1 px-2.5 py-2 text-xs font-semibold
                                                rounded-xl border transition-all duration-300
                                                hover:-translate-y-0.5
                                               ${peca.statuspeca === "ESGOTADO"
                                                        ? "text-yellow-400 bg-yellow-500/10 border-yellow-500/30 hover:text-white hover:bg-yellow-500 hover:border-yellow-400 hover:shadow-lg hover:shadow-yellow-500/20"
                                                        : peca.statuspeca === "EXCLUIDO"
                                                            ? "text-red-400 bg-red-500/10 border-red-500/30 hover:text-white hover:bg-red-500 hover:border-red-400 hover:shadow-lg hover:shadow-red-500/20"
                                                            : "text-green-400 bg-green-500/10 border-green-500/30 hover:text-white hover:bg-green-500 hover:border-green-400 hover:shadow-lg hover:shadow-green-500/20"
                                                    }`}
                                            >
                                                {peca.statuspeca}
                                            </button>

                                        </div>

                                    </td>

                                </tr>

                            ))}

                            {pecas.length === 0 && (

                                <tr>

                                    <td
                                        colSpan={8}
                                        className="px-6 py-12 text-center font-medium text-slate-100"
                                    >
                                        Nenhuma peça encontrada.
                                    </td>

                                </tr>

                            )}

                        </tbody>

                    </table>

                </div>

            </div>

        </div>
    );
}