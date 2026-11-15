
'use client'

import {
    atualizarOrdemServico,
    salvarOrdemServico,
    associarPecaOrdemServico,
    listarPecasPorOrdemServico,
    atualizarPecaOrdemServico,
    excluirPecaOrdemServico,
    PecaOrdemServico
} from "@/app/services/ordemServicoService";

import { buscarListaPeca } from "@/app/services/pecaService";
import { buscarListaVeiculos } from "@/app/services/veiculoService";

import { OrdemServico } from "@/app/types/ordemservico";
import { Peca } from "@/app/types/peca";
import { veiculo } from "@/app/types/veiculo";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface OrdemServicoFormProps {
    ordemServicoExistente?: OrdemServico | null;
}

interface PecaSelecionada {
    id: number | null;
    pecaId: number;
    nome: string;
    quantidade: number;
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
            "",
            null
        )
    );

    const [veiculos, setVeiculos] = useState<veiculo[]>([]);
    const [pecas, setPecas] = useState<Peca[]>([]);

    const [veiculoSelecionado, setVeiculoSelecionado] = useState<string>(
        ordemServicoExistente?.veiculoId?.toString() || ""
    );

    const [pecaSelecionada, setPecaSelecionada] = useState("");
    const [quantidadePeca, setQuantidadePeca] = useState("1");

    const [pecasSelecionadas, setPecasSelecionadas] = useState<PecaSelecionada[]>([]);
    const [pecasOriginais, setPecasOriginais] = useState<PecaSelecionada[]>([]);

    useEffect(() => {

        async function carregarDados() {
            try {
                const listaVeiculos = await buscarListaVeiculos();
                const listaPecas = await buscarListaPeca();

                setVeiculos(listaVeiculos);
                setPecas(listaPecas);

                if (ordemServicoExistente?.id) {

                    const associacoes: PecaOrdemServico[] =
                        await listarPecasPorOrdemServico(ordemServicoExistente.id);

                    const pecasCarregadas: PecaSelecionada[] = associacoes.map(item => {

                        const peca = listaPecas.find(
                            p => p.id === item.pecaId
                        );

                        return {
                            id: item.id,
                            pecaId: item.pecaId,
                            nome: peca?.nome || `Peça ${item.pecaId}`,
                            quantidade: item.quantidade
                        };
                    });

                    setPecasSelecionadas(pecasCarregadas);
                    setPecasOriginais(pecasCarregadas);
                }

            } catch (erro) {
                console.error("Erro ao carregar veículos e peças:", erro);
                alert("Erro ao carregar veículos e peças.");
            }
        }

        carregarDados();

    }, [ordemServicoExistente?.id]);

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
                    : valorAnterior.descricao,

                valorAnterior.veiculoId
            )
        );
    };

    const adicionarPeca = () => {

        const peca = pecas.find(
            item => item.id === Number(pecaSelecionada)
        );

        const quantidade = Number(quantidadePeca);

        if (!peca) {
            alert("Selecione uma peça.");
            return;
        }

        if (!Number.isInteger(quantidade) || quantidade <= 0) {
            alert("Informe uma quantidade válida.");
            return;
        }

        if (peca.statusPeca !== "DISPONIVEL") {
            alert("Esta peça não está disponível.");
            return;
        }

        if (pecasSelecionadas.some(item => item.pecaId === peca.id)) {
            alert("Esta peça já foi adicionada à ordem de serviço. Altere a quantidade na lista.");
            return;
        }

        const estoque = Number(peca.quantidade);

        const quantidadeJaAdicionada = pecasSelecionadas
            .filter(item => item.pecaId === peca.id)
            .reduce((total, item) => total + item.quantidade, 0);

        if (quantidadeJaAdicionada + quantidade > estoque) {
            alert("Quantidade maior que o estoque disponível.");
            return;
        }

        setPecasSelecionadas(lista => [
            ...lista,
            {
                id: null,
                pecaId: peca.id!,
                nome: peca.nome,
                quantidade
            }
        ]);

        setPecaSelecionada("");
        setQuantidadePeca("1");
    };

    const alterarQuantidade = (indice: number, quantidade: number) => {

        setPecasSelecionadas(lista =>
            lista.map((item, index) =>
                index === indice
                    ? { ...item, quantidade }
                    : item
            )
        );
    };

    const removerPeca = (indice: number) => {

        setPecasSelecionadas(lista =>
            lista.filter((_, index) => index !== indice)
        );
    };

    const salvarPecas = async (ordemServicoId: number) => {

        // Exclui os vínculos removidos
        const pecasRemovidas = pecasOriginais.filter(original =>
            !pecasSelecionadas.some(atual => atual.id === original.id)
        );

        for (const item of pecasRemovidas) {
            if (item.id !== null) {
                await excluirPecaOrdemServico(item.id);
            }
        }

        // Atualiza as quantidades dos vínculos existentes
        const pecasAlteradas = pecasSelecionadas.filter(atual => {

            if (atual.id === null) return false;

            const original = pecasOriginais.find(
                item => item.id === atual.id
            );

            return original && original.quantidade !== atual.quantidade;
        });

        for (const item of pecasAlteradas) {

            await atualizarPecaOrdemServico(
                item.id!,
                ordemServicoId,
                item.pecaId,
                item.quantidade
            );
        }

        // Cadastra os novos vínculos
        const pecasNovas = pecasSelecionadas.filter(
            item => item.id === null
        );

        for (const item of pecasNovas) {

            await associarPecaOrdemServico(
                ordemServicoId,
                item.pecaId,
                item.quantidade
            );
        }
    };

    const handlerSalvar = async () => {

        if (!veiculoSelecionado) {
            alert("Selecione um veículo.");
            return;
        }

        try {

            const ordemParaSalvar = new OrdemServico(
                ordemServico.id,
                ordemServico.dataAbertura,
                ordemServico.dataConclusao,
                ordemServico.statusOrdemServico,
                ordemServico.descricao,
                Number(veiculoSelecionado)
            );

            if (ordemServicoExistente) {

                const dadosRetorno = await atualizarOrdemServico(ordemParaSalvar);

                if (!dadosRetorno) {
                    alert("Erro ao atualizar a Ordem de Serviço!");
                    return;
                }

                await salvarPecas(Number(ordemServicoExistente.id));

                alert("A Ordem de Serviço foi atualizada com sucesso!");

            } else {

                const ordemSalva = await salvarOrdemServico(ordemParaSalvar);

                if (!ordemSalva || !ordemSalva.id) {
                    alert("Erro ao salvar a Ordem de Serviço!");
                    return;
                }

                for (const item of pecasSelecionadas) {

                    await associarPecaOrdemServico(
                        ordemSalva.id,
                        item.pecaId,
                        item.quantidade
                    );
                }

                alert("A Ordem de Serviço foi salva com sucesso!");
            }

            router.push("/ordemServico");

        } catch (erro: any) {

            console.error("Erro ao salvar Ordem de Serviço:", erro);

            alert(
                erro.response?.data ||
                "Erro ao salvar a Ordem de Serviço."
            );
        }
    };

    return (

        <form
            action={handlerSalvar}
            className="max-w-lg mx-auto bg-neutral-900 border border-neutral-800 rounded-2xl p-8"
        >

            <div className="flex flex-col gap-5">

                {/* VEÍCULO */}
                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Veículo:
                    </label>

                    <select
                        value={veiculoSelecionado}
                        required
                        onChange={(e) => setVeiculoSelecionado(e.target.value)}
                        className="w-full bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5"
                    >
                        <option value="">Selecione um veículo</option>

                        {veiculos
                            .filter(item => item.statusVeiculo === "ATIVO")
                            .map(item => (
                                <option key={item.id} value={item.id!}>
                                    {item.modelo} - {item.placa}
                                </option>
                            ))}
                    </select>

                </div>

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
                            handlerChange('dataAbertura', e.target.value)
                        }
                        className="w-full bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5"
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
                            handlerChange('dataConclusao', e.target.value)
                        }
                        className="w-full bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5"
                    />

                </div>

                {/* PEÇAS */}
                <div className="border-t border-neutral-700 pt-5 flex flex-col gap-3">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Adicionar peças:
                    </label>

                    <select
                        value={pecaSelecionada}
                        onChange={(e) => setPecaSelecionada(e.target.value)}
                        className="w-full bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5"
                    >
                        <option value="">Selecione uma peça</option>

                        {pecas
                            .filter(item => item.statusPeca === "DISPONIVEL")
                            .map(item => (
                                <option key={item.id} value={item.id!}>
                                    {item.nome} - Estoque: {item.quantidade}
                                </option>
                            ))}
                    </select>

                    <div className="flex gap-3">

                        <input
                            type="number"
                            min="1"
                            value={quantidadePeca}
                            onChange={(e) => setQuantidadePeca(e.target.value)}
                            placeholder="Quantidade"
                            className="flex-1 bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5"
                        />

                        <button
                            type="button"
                            onClick={adicionarPeca}
                            className="px-4 py-2 bg-orange-500 text-black font-bold rounded-lg hover:bg-orange-400"
                        >
                            Adicionar
                        </button>

                    </div>

                    {/* LISTA DE PEÇAS */}
                    {pecasSelecionadas.map((item, indice) => (

                        <div
                            key={item.id ?? `nova-${item.pecaId}`}
                            className="flex justify-between items-center gap-3 bg-neutral-800 rounded-lg p-3"
                        >

                            <span className="text-sm text-white flex-1">
                                {item.nome}
                            </span>

                            <input
                                type="number"
                                min="1"
                                value={item.quantidade}
                                onChange={(e) =>
                                    alterarQuantidade(indice, Number(e.target.value))
                                }
                                className="w-20 bg-neutral-700 border border-neutral-600 text-white rounded-lg px-2 py-1"
                            />

                            <button
                                type="button"
                                onClick={() => removerPeca(indice)}
                                className="text-red-400 hover:text-red-300 text-sm"
                            >
                                Remover
                            </button>

                        </div>

                    ))}

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
                            handlerChange('descricao', e.target.value)
                        }
                        placeholder="Descreva o serviço realizado..."
                        rows={4}
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5"
                    />

                </div>

                {/* BOTÕES */}
                <div className="flex items-center justify-end gap-4 mt-2">

                    <Link
                        href="/ordemServico"
                        className="text-sm font-semibold text-neutral-400 hover:text-white"
                    >
                        Cancelar
                    </Link>

                    <button
                        type="submit"
                        className="px-6 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-lg"
                    >
                        Salvar
                    </button>

                </div>

            </div>

        </form>
    );
}
