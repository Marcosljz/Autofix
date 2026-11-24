"use client"

import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import api from "@/app/services/api";
import { RootState, AppDispatch } from "@/app/redux/store";
import { definirHistoricos } from "@/app/redux/slices/historicoSlice";
import { Historico } from "@/app/types/historico";
import { buscarListaHistorico } from "@/app/services/historicoService";

export default function HistoricoPage() {

    const dispatch = useDispatch<AppDispatch>();

    const historicos = useSelector(
        (state: RootState) => state.historico.historicos
    );

    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState("");

    useEffect(() => {

       const carregarHistorico = async () => {

    try {

        const dados = await buscarListaHistorico();

        dispatch(definirHistoricos(dados));

    } catch (error) {

        setErro("Erro ao carregar o histórico.");

    } finally {

        setCarregando(false);

    }
};

carregarHistorico();

    }, [dispatch]);

    const formatarData = (data: string) => {

        return new Date(data).toLocaleString("pt-BR");

    };

    return (

        <div className="min-h-screen bg-black px-6 py-10">

            <div className="max-w-5xl mx-auto mb-8">

                <h1 className="text-2xl font-bold text-white">
                    Histórico de atividades
                </h1>

                <p className="text-sm text-neutral-400 mt-2">
                    Consulte as atividades registradas no sistema.
                </p>

            </div>

            <div className="max-w-5xl mx-auto">

                <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-hidden">

                    {carregando ? (

                        <div className="px-6 py-12 text-center text-neutral-400">
                            Carregando histórico...
                        </div>

                    ) : erro ? (

                        <div className="px-6 py-12 text-center text-red-400">
                            {erro}
                        </div>

                    ) : historicos.length === 0 ? (

                        <div className="px-6 py-12 text-center text-neutral-400">
                            Nenhuma atividade encontrada.
                        </div>

                    ) : (

                        <div className="overflow-x-auto">

                            <table className="w-full text-left">

                                <thead className="bg-neutral-800/50">

                                    <tr>

                                        <th className="px-6 py-4 text-xs font-semibold text-neutral-400 uppercase">
                                            Data e hora
                                        </th>

                                        <th className="px-6 py-4 text-xs font-semibold text-neutral-400 uppercase">
                                            Descrição
                                        </th>

                                        <th className="px-6 py-4 text-xs font-semibold text-neutral-400 uppercase">
                                            Usuário
                                        </th>

                                        <th className="px-6 py-4 text-xs font-semibold text-neutral-400 uppercase">
                                            Mecânica
                                        </th>

                                    </tr>

                                </thead>

                                <tbody className="divide-y divide-neutral-800">

                                    {historicos.map((historico) => (

                                        <tr
                                            key={historico.id}
                                            className="hover:bg-neutral-800/40 transition-colors"
                                        >

                                            <td className="px-6 py-4 text-sm text-neutral-300 whitespace-nowrap">
                                                {formatarData(historico.dataHora)}
                                            </td>

                                            <td className="px-6 py-4 text-sm text-white">
                                                {historico.descricao}
                                            </td>

                                            <td className="px-6 py-4 text-sm text-neutral-300">
                                                {historico.usuario}
                                            </td>

                                            <td className="px-6 py-4 text-sm text-neutral-300">
                                                {historico.mecanica}
                                            </td>

                                        </tr>

                                    ))}

                                </tbody>

                            </table>

                        </div>

                    )}

                </div>

            </div>

        </div>

    );
}