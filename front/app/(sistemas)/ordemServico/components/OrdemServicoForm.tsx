'use client'

import { OrdemServico } from "@/app/types/ordemservico";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface OrdemServicoFormProps {
    ordemServicoExistente?: OrdemServico | null;
}

export default function OrdemServicoForm({
    ordemServicoExistente
}: OrdemServicoFormProps) {

    const router = useRouter();

    const [ordemServico, setOrdemServico] = useState<OrdemServico>(
        ordemServicoExistente || new OrdemServico(
            null,
            new Date(),
            new Date(),
            "ABERTO",
            ""
        )
    );


    const handlerChange = (
        campo: 'dataAbertura' | 'dataConclusao' | 'descricao',
        valor: string
    ) => {

        setOrdemServico(valorAnterior =>
            new OrdemServico(
                valorAnterior.id,

                campo === 'dataAbertura'
                    ? new Date(valor)
                    : valorAnterior.dataAbertura,

                campo === 'dataConclusao'
                    ? new Date(valor)
                    : valorAnterior.dataConclusao,

                valorAnterior.statusOrdemServico,

                campo === 'descricao'
                    ? valor
                    : valorAnterior.descricao
            )
        );
    }

    const handlerSalvar = async () => {

        if (ordemServicoExistente) {

            const dadosRetorno = await axios.put(
                'http://localhost:8080/ordemservicos/' + ordemServico.id,
                ordemServico
            );

            if (dadosRetorno.status === 200) {

                alert("Ordem de serviço foi salva com sucesso!");

            } else {

                alert(dadosRetorno.data);
                return;

            }

        } else {

            const dadosRetorno = await axios.post(
                'http://localhost:8080/ordemservicos',
                ordemServico
            );

            if (
                dadosRetorno.status === 200 ||
                dadosRetorno.status === 201
            ) {

                alert("Ordem de serviço foi cadastrada com sucesso!");

            } else {

                alert(dadosRetorno.data);
                return;

            }
        }

        router.push("/ordemServico");
    }


    return (

        <form
            action={handlerSalvar}
            className="max-w-lg mx-auto bg-neutral-900 border border-neutral-800 rounded-2xl p-8"
        >

            <div className="flex flex-col gap-5">

                {/* DATA DE ABERTURA */}
                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Data de abertura:
                    </label>

                    <input
                        name="dataAbertura"
                        type="datetime-local"
                        value={
                            ordemServico.dataAbertura
                                ? new Date(String(ordemServico.dataAbertura))
                                    .toISOString()
                                    .slice(0, 16)
                                : ""
                        }
                        required
                        onChange={(e) =>
                            handlerChange(
                                'dataAbertura',
                                e.target.value
                            )
                        }
                        className="w-full bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />

                </div>


                {/* DATA DE CONCLUSÃO */}
                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Data de conclusão:
                    </label>

                    <input
                        name="dataConclusao"
                        type="datetime-local"
                        value={
                            ordemServico.dataConclusao
                                ? new Date(String(ordemServico.dataConclusao))
                                    .toISOString()
                                    .slice(0, 16)
                                : ""
                        }
                        onChange={(e) =>
                            handlerChange(
                                'dataConclusao',
                                e.target.value
                            )
                        }
                        className="w-full bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />

                </div>


                {/* DESCRIÇÃO */}
                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Descrição:
                    </label>

                    <textarea
                        name="descricao"
                        value={ordemServico.descricao}
                        required
                        onChange={(e) =>
                            handlerChange(
                                'descricao',
                                e.target.value
                            )
                        }
                        placeholder="Descreva o serviço realizado..."
                        rows={4}
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />

                </div>


                {/* BOTÕES */}
                <div className="flex items-center justify-end gap-4 mt-2">

                    <Link
                        href="/ordemServico"
                        className="text-sm font-semibold text-neutral-400 hover:text-white transition-colors"
                    >
                        Cancelar
                    </Link>

                    <button
                        type="submit"
                        className="px-6 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-lg transition-colors active:scale-95"
                    >
                        Salvar
                    </button>

                </div>

            </div>

        </form>
    );
}