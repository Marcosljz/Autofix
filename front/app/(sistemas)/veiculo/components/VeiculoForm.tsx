'use client'
import { veiculo } from "@/app/types/veiculo";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";


interface VeiculoFormProps {
    veiculoExistente?: veiculo | null;
}


export default function VeiculoForm({ veiculoExistente }: VeiculoFormProps) {

    const router = useRouter();

    const [veiculoDados, setVeiculoDados] = useState<veiculo>(
        veiculoExistente || new veiculo(
            null,
            "",
            "",
            "",
            "",
            0,
            "ATIVO"
        )
    );


    const handlerChange = (
        campo: 'placa' | 'modelo' | 'ano' | 'cor' | 'quilometragem',
        valor: string
    ) => {

        setVeiculoDados(valorAnterior =>
            new veiculo(
                valorAnterior.id,

                campo === 'placa'
                    ? valor
                    : valorAnterior.placa,

                campo === 'modelo'
                    ? valor
                    : valorAnterior.modelo,

                campo === 'ano'
                    ? valor
                    : valorAnterior.ano,

                campo === 'cor'
                    ? valor
                    : valorAnterior.cor,

                campo === 'quilometragem'
                    ? Number(valor)
                    : valorAnterior.quilometragem,

                valorAnterior.statusVeiculo
            )
        );
    }


    const handlerSalvar = async (formData: FormData) => {

        if (veiculoExistente) {

            var dadosRetorno = await axios.put<number>(
                'http://localhost:8080/veiculos/' + veiculoDados.id,
                veiculoDados
            );

            if (dadosRetorno.status === 200) {

                alert("Veículo foi salvo com sucesso!");

            } else {

                alert(dadosRetorno.data);
                return;

            }

        } else {

            var dadosRetorno = await axios.post<number>(
                'http://localhost:8080/veiculos',
                veiculoDados
            );

            if (dadosRetorno.status === 200 || dadosRetorno.status === 201) {

                alert("Veículo foi cadastrado com sucesso!");

            } else {

                alert(dadosRetorno.data);
                return;

            }
        }

        router.push("/veiculo");
    }


    return (

        <form
            action={handlerSalvar}
            className="max-w-lg mx-auto bg-neutral-900 border border-neutral-800 rounded-2xl p-8"
        >

            <div className="flex flex-col gap-5">

                {/* PLACA */}
                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Placa:
                    </label>

                    <input
                        name="placa"
                        value={veiculoDados.placa}
                        required
                        onChange={(e) =>
                            handlerChange('placa', e.target.value)
                        }
                        placeholder="ABC-1234"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />

                </div>


                {/* MODELO */}
                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Modelo:
                    </label>

                    <input
                        name="modelo"
                        value={veiculoDados.modelo}
                        required
                        onChange={(e) =>
                            handlerChange('modelo', e.target.value)
                        }
                        placeholder="Civic"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />

                </div>


                {/* ANO */}
                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Ano:
                    </label>

                    <input
                        name="ano"
                        value={veiculoDados.ano}
                        required
                        onChange={(e) =>
                            handlerChange('ano', e.target.value)
                        }
                        placeholder="2025"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />

                </div>


                {/* COR */}
                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Cor:
                    </label>

                    <input
                        name="cor"
                        value={veiculoDados.cor}
                        required
                        onChange={(e) =>
                            handlerChange('cor', e.target.value)
                        }
                        placeholder="Preto"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />

                </div>


                {/* QUILOMETRAGEM */}
                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Quilometragem:
                    </label>

                    <input
                        name="quilometragem"
                        type="number"
                        value={veiculoDados.quilometragem}
                        required
                        onChange={(e) =>
                            handlerChange('quilometragem', e.target.value)
                        }
                        placeholder="50000"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />

                </div>


                {/* BOTÕES */}
                <div className="flex items-center justify-end gap-4 mt-2">

                    <Link
                        href="/veiculos"
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