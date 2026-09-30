"use client"

import { OrdemServico } from "@/app/types/ordemservico";
import axios from "axios";
import Link from "next/link";
import { useEffect, useState } from "react";


export default function OrdensServico() {

    const [ordensServico, setOrdensServico] = useState<OrdemServico[]>([]);


    useEffect(() => {
        carregarDados();
    }, []);


    const carregarDados = async () => {

        try {

            const dados = await axios.get<OrdemServico[]>(
                "http://localhost:8080/ordemservicos"
            );

            setOrdensServico(dados.data);

        } catch (error) {

            alert("Erro ao carregar dados do servidor!");

        }

    }


    const handleDeletarOrdemServico = async (
        ordemServico: OrdemServico
    ) => {

        const dadosRetorno = await axios.delete(
            "http://localhost:8080/ordemservicos/" + ordemServico.id +
            "/excluir"
        );


        if (dadosRetorno.status == 200) {

            alert("Excluído com sucesso!");

        } else {

            alert(dadosRetorno.data);
            return;

        }

        carregarDados();

    }


    const handleAlterarStatusOrdemServico = async (
        ordemServico: OrdemServico
    ) => {

        let novoStatus = {};

        if (ordemServico.statusOrdemServico === "ABERTO") {

            novoStatus = {
                statusOrdemServico: "ANDAMENTO"
            };

        } else if (ordemServico.statusOrdemServico === "ANDAMENTO") {

            novoStatus = {
                statusOrdemServico: "FECHADO"
            };

        } else if (ordemServico.statusOrdemServico === "FECHADO") {

            novoStatus = {
                statusOrdemServico: "ABERTO"
            };

        } else if (ordemServico.statusOrdemServico === "EXCLUIDO") {

            novoStatus = {
                statusOrdemServico: "ABERTO"
            };

        }


        const dadosRetorno = await axios.patch(
            "http://localhost:8080/ordemservicos/" +
            ordemServico.id +
            "/status",
            novoStatus
        );


        if (dadosRetorno.status == 200) {

            alert("Status atualizado com sucesso!");

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
                    Ordens de Serviço
                </h1>


                <Link
                    href="/ordemServico/novo"
                    className="inline-flex items-center justify-center px-5 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-xl transition-colors active:scale-95"
                >
                    Nova ordem de serviço
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
                                    Data Abertura
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Data Conclusão
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Status
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Descrição
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Ações
                                </th>

                            </tr>

                        </thead>


                        <tbody className="divide-y divide-slate-800 text-slate-200">


                            {ordensServico.map((ordemServico) => (

                                <tr
                                    key={ordemServico.id}
                                    className="hover:bg-slate-800/40 transition-colors"
                                >

                                    <td className="px-6 py-4 text-sm text-white">
                                        {ordemServico.id}
                                    </td>


                                    <td className="px-6 py-4 text-sm text-white">
                                        {new Date(ordemServico.dataAbertura).toISOString().slice(0, 16)}
                                    </td>


                                    <td className="px-6 py-4 text-sm text-white">
                                        {ordemServico.dataConclusao
                                            ? new Date(ordemServico.dataConclusao).toISOString().slice(0, 16)
                                            : "-"
                                        }
                                    </td>


                                    <td className="px-6 py-4 text-sm text-white">
                                        {ordemServico.statusOrdemServico}
                                    </td>


                                    <td className="px-6 py-4 text-sm text-white">
                                        {ordemServico.descricao}
                                    </td>


                                    <td className="px-6 py-4 text-sm font-medium">

                                       <div className="flex items-center justify-end gap-1">

    {/* EDITAR */}

    <Link
        href={`/ordemServico/${ordemServico.id}/editar`}
        className="group inline-flex items-center gap-2 px-2.5 py-2 text-xs font-semibold
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
            className="w-4 h-4"
        >
            <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Z"
            />

            <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M19.5 7.125 16.875 4.5"
            />
        </svg>

        Editar

    </Link>


    {/* DELETAR */}

    <button
        type="button"
        onClick={() => handleDeletarOrdemServico(ordemServico)}
        className="group inline-flex items-center gap-2 px-2.5 py-2 text-xs font-semibold
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
            className="w-4 h-4"
        >
            <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0C7.91 2.878 7 3.862 7 5.042v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"
            />
        </svg>

        Deletar

    </button>


    {/* STATUS */}

    <button
        type="button"
        onClick={() => handleAlterarStatusOrdemServico(ordemServico)}
        className={`group inline-flex items-center gap-2 px-2.5 py-2 text-xs font-semibold
        rounded-xl border transition-all duration-300
        hover:-translate-y-0.5
        ${
            ordemServico.statusOrdemServico === "ABERTO"
                ? "text-green-400 bg-green-500/10 border-green-500/30 hover:text-white hover:bg-green-500 hover:border-green-400 hover:shadow-lg hover:shadow-green-500/20"
                : ordemServico.statusOrdemServico === "ANDAMENTO"
                ? "text-yellow-400 bg-yellow-500/10 border-yellow-500/30 hover:text-white hover:bg-yellow-500 hover:border-yellow-400 hover:shadow-lg hover:shadow-yellow-500/20"
                : ordemServico.statusOrdemServico === "FECHADO"
                ? "text-blue-400 bg-blue-500/10 border-blue-500/30 hover:text-white hover:bg-blue-500 hover:border-blue-400 hover:shadow-lg hover:shadow-blue-500/20"
                : "text-red-400 bg-red-500/10 border-red-500/30 hover:text-white hover:bg-red-500 hover:border-red-400 hover:shadow-lg hover:shadow-red-500/20"
        }`}
    >
                                                {ordemServico.statusOrdemServico}
                                            </button>

                                        </div>

                                    </td>

                                </tr>

                            ))}


                            {ordensServico.length === 0 && (

                                <tr>

                                    <td
                                        colSpan={6}
                                        className="px-6 py-12 text-center font-medium text-slate-100"
                                    >
                                        Nenhuma ordem de serviço encontrada.
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