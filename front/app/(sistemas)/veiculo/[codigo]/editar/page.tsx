"use client"

import Link from "next/link";

import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { veiculo } from "@/app/types/veiculo";
import axios from "axios";
import VeiculoForm from "../../components/VeiculoForm";


export default function EditarVeiculo() {

    const router = useRouter();

    const parametro = useParams();

    const codigo = Number(parametro.codigo);

    const [veiculoDados, setVeiculoDados] = useState<veiculo | null>(null);


    useEffect(() => {

        buscarDados();

    }, []);


    const buscarDados = async () => {

        const valorVeiculoBack = await axios.get<veiculo>(
            'http://localhost:8080/veiculos/' + codigo
        );

        if (valorVeiculoBack.status == 200) {

            setVeiculoDados(valorVeiculoBack.data);

        } else {

            router.push("/veiculo");

        }

    }


    if (!veiculoDados) {
        return (
            <div className="P-8">
                carregando Dados ...
            </div>
        )
    }


    return (

        <div className="h-full w-full px-8 py-8">

            <div className="max-w-3xl mx-auto">

                <div className="mb-6">

                    <Link
                        href="/veiculo"
                        className="inline-flex items-center gap-1.5 text-xs font-semibold text-neutral-500 hover:text-orange-500 transition-colors"
                    >

                        <svg
                            className="w-3.5 h-3.5"
                            fill="none"
                            stroke="currentColor"
                            strokeWidth={2.5}
                            viewBox="0 0 24 24"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                d="M15 19l-7-7 7-7"
                            />
                        </svg>

                        Voltar para Listagem

                    </Link>


                    <div className="mt-3 flex items-center justify-between">

                        <div>

                            <h1 className="text-2xl font-bold text-white">
                                Editar veículo {codigo}
                            </h1>

                            <p className="text-sm text-neutral-400 mt-1">
                                Preencha os dados para editar o veículo
                            </p>

                        </div>


                        <span className="hidden sm:inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-orange-500/10 text-orange-400 border border-orange-500/20">
                            Cadastro
                        </span>

                    </div>

                </div>


                <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-hidden">

                    <div className="px-6 py-4 border-b border-neutral-800">

                        <h2 className="text-sm font-semibold text-white">
                            Dados do veículo
                        </h2>

                    </div>


                    <div className="p-6">

                        <VeiculoForm
                            veiculoExistente={veiculoDados}
                        />

                    </div>

                </div>

            </div>

        </div>

    )
}