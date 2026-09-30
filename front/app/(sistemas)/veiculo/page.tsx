"use client"

import { veiculo } from "@/app/types/veiculo";
import axios from "axios";
import Link from "next/link";
import { useEffect, useState } from "react";


export default function Veiculos() {

    const [veiculos, setVeiculos] = useState<veiculo[]>([]);


    useEffect(() => {
        carregarDados();
    }, []);


    const carregarDados = async () => {

        try {

            const dados = await axios.get<veiculo[]>(
                "http://localhost:8080/veiculos"
            );

            setVeiculos(dados.data);

        } catch (error) {

            alert("Erro ao carregar dados do servidor!");

        }
    }


    const handleDeletarVeiculo = async (veiculo: veiculo) => {

        var dadosRetorno = await axios.delete(
            'http://localhost:8080/veiculos/' + veiculo.id + '/excluir'
        );

        if (dadosRetorno.status == 200) {

            alert("Excluído com sucesso!");

        } else {

            alert(dadosRetorno.data);
            return;

        }

        carregarDados();
    }


    const handleAlterarStatusVeiculo = async (veiculo: veiculo) => {

        var novoStatus = {};

        if (veiculo.statusVeiculo === "ATIVO") {

            novoStatus = {
                statusVeiculo: "INATIVO"
            };

        } else {

            novoStatus = {
                statusVeiculo: "ATIVO"
            };

        }


        var dadosRetorno = await axios.patch(
            'http://localhost:8080/veiculos/' + veiculo.id + '/status',
            novoStatus
        );


        if (dadosRetorno.status == 200) {

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
                    Veículos
                </h1>


                <Link
                    href="/veiculo/novo"
                    className="inline-flex items-center justify-center px-5 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-xl transition-colors active:scale-95"
                >
                    Novo veículo
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
                                    Placa
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Modelo
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Ano
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Cor
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Quilometragem
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Status
                                </th>

                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">
                                    Ações
                                </th>

                            </tr>

                        </thead>


                        <tbody className="divide-y divide-slate-800 text-slate-200">

                            {veiculos.map((veiculo) => (

                                <tr
                                    key={veiculo.id}
                                    className="hover:bg-slate-800/40 transition-colors"
                                >

                                    <td className="px-6 py-4 text-sm text-white">
                                        {veiculo.id}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {veiculo.placa}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {veiculo.modelo}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {veiculo.ano}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {veiculo.cor}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {veiculo.quilometragem}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {veiculo.statusVeiculo}
                                    </td>


                                    <td className="px-6 py-4 text-sm font-medium">

                                        <div className="flex items-center justify-end gap-2">

    <Link
        href={`/veiculo/${veiculo.id}/editar`}
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
        onClick={() => handleDeletarVeiculo(veiculo)}
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
                d="M6 7h12M9 7V5h6v2m-7 4v6m4-6v6M5 7l1 14h12l1-14"
            />
        </svg>

        Deletar
    </button>


    <button
        type="button"
        onClick={() => handleAlterarStatusVeiculo(veiculo)}
        className={`group inline-flex items-center gap-1 px-2.5 py-2 text-xs font-semibold
        rounded-xl border transition-all duration-300
        hover:-translate-y-0.5
        ${
            veiculo.statusVeiculo === "ATIVO"
                ? "text-green-400 bg-green-500/10 border-green-500/30 hover:text-white hover:bg-green-500 hover:border-green-400 hover:shadow-lg hover:shadow-green-500/20"
                : veiculo.statusVeiculo === "INATIVO"
                ? "text-yellow-400 bg-yellow-500/10 border-yellow-500/30 hover:text-white hover:bg-yellow-500 hover:border-yellow-400 hover:shadow-lg hover:shadow-yellow-500/20"
                : "text-red-400 bg-red-500/10 border-red-500/30 hover:text-white hover:bg-red-500 hover:border-red-400 hover:shadow-lg hover:shadow-red-500/20"
        }`}
    >
        {veiculo.statusVeiculo}
    </button>

</div>

                                    </td>

                                </tr>

                            ))}


                            {veiculos.length === 0 && (

                                <tr>

                                    <td
                                        colSpan={8}
                                        className="px-6 py-12 text-center font-medium text-slate-100"
                                    >
                                        Nenhum veículo encontrado.
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