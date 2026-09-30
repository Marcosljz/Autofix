'use client'

import { Peca } from "@/app/types/peca";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface PecaFormProps {
    pecaExistente?: Peca | null;
}

export default function PecaForm({ pecaExistente }: PecaFormProps) {

    const router = useRouter();

    const [peca, setPeca] = useState<Peca>(
        pecaExistente || new Peca(null, "", "", "", "", "", "DISPONIVEL")
    );

    const handlerChange = (
        campo: 'nome' | 'preco' | 'marca' | 'descricao' | 'quantidade',
        valor: string
    ) => {

        setPeca(valorAnterior =>
            new Peca(
                valorAnterior.id,
                campo === 'nome' ? valor : valorAnterior.nome,
                campo === 'preco' ? valor : valorAnterior.preco,
                campo === 'marca' ? valor : valorAnterior.marca,
                campo === 'descricao' ? valor : valorAnterior.descricao,
                campo === 'quantidade' ? valor : valorAnterior.quantidade,
                valorAnterior.statuspeca
            )
        );
    }

   const handlerSalvar = async () => {

        if (pecaExistente) {

            // MANTÉM A ROTA DO SEU BACKEND
            var dadosRetorno = await axios.put(
                'http://localhost:8080/pecas/' + peca.id,
                peca
            );

            if (dadosRetorno.status === 200) {
                alert("Peça foi salva com sucesso!");
            } else {
                alert(dadosRetorno.data);
                return;
            }

        } else {

            // MANTÉM A ROTA DO SEU BACKEND
            var dadosRetorno = await axios.post(
                'http://localhost:8080/pecas',
                peca
            );

            if (dadosRetorno.status === 200) {
                alert("Peça foi salva com sucesso!");
            } else {
                alert(dadosRetorno.data);
                return;
            }
        }

        // ROTA DO FRONTEND
        router.push("/peca");
    }

    return (
        <form
            action={handlerSalvar}
            className="max-w-lg mx-auto bg-neutral-900 border border-neutral-800 rounded-2xl p-8"
        >
            <div className="flex flex-col gap-5">

                <div className="flex flex-col gap-2">
                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Nome:
                    </label>

                    <input
                        name="nome"
                        value={peca.nome}
                        required
                        onChange={(e) => handlerChange('nome', e.target.value)}
                        placeholder="Nome da peça"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />
                </div>

                <div className="flex flex-col gap-2">
                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Preço:
                    </label>

                    <input
                        name="preco"
                        value={peca.preco}
                        required
                        onChange={(e) => handlerChange('preco', e.target.value)}
                        placeholder="35.90"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />
                </div>

                <div className="flex flex-col gap-2">
                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Marca:
                    </label>

                    <input
                        name="marca"
                        value={peca.marca}
                        required
                        onChange={(e) => handlerChange('marca', e.target.value)}
                        placeholder="Bosch"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />
                </div>

                <div className="flex flex-col gap-2">
                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Quantidade:
                    </label>

                    <input
                        name="quantidade"
                        value={peca.quantidade}
                        required
                        onChange={(e) => handlerChange('quantidade', e.target.value)}
                        placeholder="10"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />
                </div>

                <div className="flex flex-col gap-2">
                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Descrição:
                    </label>

                    <textarea
                        name="descricao"
                        value={peca.descricao}
                        required
                        onChange={(e) => handlerChange('descricao', e.target.value)}
                        placeholder="Descrição da peça"
                        rows={4}
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                    />

                </div>

                <div className="flex items-center justify-end gap-4 mt-2">

                    {/* FRONTEND */}
                    <Link
                        href="/peca"
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