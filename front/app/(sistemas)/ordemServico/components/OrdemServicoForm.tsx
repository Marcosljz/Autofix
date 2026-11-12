"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { atualizarOrdemServico, salvarOrdemServico, adicionarPecaNaOrdemServico } from "@/app/services/ordemServicoService";
import { buscarListaVeiculos } from "@/app/services/veiculoService";
import { buscarListaPeca } from "@/app/services/pecaService";
import { OrdemServico } from "@/app/types/ordemservico";
import { veiculo } from "@/app/types/veiculo";
import { Peca } from "@/app/types/peca";

interface OrdemServicoFormProps {
  ordemServicoExistente?: OrdemServico | null;
}

function dataParaCampo(valor: Date | string | null): string {
  if (!valor) return "";
  const data = new Date(valor);
  if (Number.isNaN(data.getTime())) return "";
  const ajustar = (numero: number) => String(numero).padStart(2, "0");
  return `${data.getFullYear()}-${ajustar(data.getMonth() + 1)}-${ajustar(data.getDate())}T${ajustar(data.getHours())}:${ajustar(data.getMinutes())}`;
}

export default function OrdemServicoForm({ ordemServicoExistente }: OrdemServicoFormProps) {
  const router = useRouter();
  const [ordemServico, setOrdemServico] = useState<OrdemServico>(
    ordemServicoExistente || new OrdemServico(null, new Date(), null, "ABERTO", "", null),
  );
  const [veiculos, setVeiculos] = useState<veiculo[]>([]);
  const [pecas, setPecas] = useState<Peca[]>([]);
  const [pecaId, setPecaId] = useState("");
  const [quantidadePeca, setQuantidadePeca] = useState("1");
  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    async function carregarOpcoes() {
      try {
        const [listaVeiculos, listaPecas] = await Promise.all([
          buscarListaVeiculos(),
          buscarListaPeca(),
        ]);
        setVeiculos(listaVeiculos.filter((item) => item.statusVeiculo === "ATIVO"));
        setPecas(listaPecas.filter((item) => item.statuspeca === "DISPONIVEL" && Number(item.quantidade) > 0));
      } catch {
        alert("Não foi possível carregar veículos e peças disponíveis.");
      } finally {
        setCarregando(false);
      }
    }
    carregarOpcoes();
  }, []);

  const handlerChange = (campo: "dataAbertura" | "dataConclusao" | "descricao" | "veiculoId", valor: string) => {
    setOrdemServico((anterior) => new OrdemServico(
      anterior.id,
      campo === "dataAbertura" ? new Date(valor) : anterior.dataAbertura,
      campo === "dataConclusao" ? (valor ? new Date(valor) : null) : anterior.dataConclusao,
      anterior.statusOrdemServico,
      campo === "descricao" ? valor : anterior.descricao,
      campo === "veiculoId" ? (valor ? Number(valor) : null) : anterior.veiculoId,
    ));
  };

  const handlerSalvar = async () => {
    if (!ordemServico.veiculoId) {
      alert("Selecione o veículo desta ordem de serviço.");
      return;
    }

    setSalvando(true);
    try {
      let ordemSalva: OrdemServico;
      if (ordemServicoExistente) {
        ordemSalva = await atualizarOrdemServico(ordemServico);
      } else {
        ordemSalva = await salvarOrdemServico(ordemServico);
      }

      if (pecaId && ordemSalva.id) {
        try {
          await adicionarPecaNaOrdemServico({
            ordemServicoId: ordemSalva.id,
            pecaId: Number(pecaId),
            quantidade: Number(quantidadePeca),
          });
          alert("Ordem de serviço salva e peça vinculada com sucesso!");
        } catch {
          alert("A ordem de serviço foi salva, mas não foi possível vincular a peça. Você pode tentar adicionar a peça editando a ordem.");
        }
      } else {
        alert(ordemServicoExistente ? "Ordem de serviço atualizada com sucesso!" : "Ordem de serviço cadastrada com sucesso!");
      }
      router.push("/ordemServico");
    } catch (erro) {
      const mensagem = erro instanceof Error ? erro.message : "Verifique os dados e tente novamente.";
      alert(`Não foi possível salvar a ordem de serviço. ${mensagem}`);
    } finally {
      setSalvando(false);
    }
  };

  const classeInput = "w-full bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500";

  return (
    <form action={handlerSalvar} className="max-w-lg mx-auto bg-neutral-900 border border-neutral-800 rounded-2xl p-8">
      <div className="flex flex-col gap-5">
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Veículo:
          <select required value={ordemServico.veiculoId ?? ""} onChange={(e) => handlerChange("veiculoId", e.target.value)} className={classeInput} disabled={carregando}>
            <option value="">Selecione um veículo</option>
            {veiculos.map((item) => <option key={item.id} value={item.id ?? ""}>{item.placa} - {item.modelo} ({item.ano})</option>)}
          </select>
        </label>

        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Data de abertura:
          <input name="dataAbertura" type="datetime-local" value={dataParaCampo(ordemServico.dataAbertura)} required onChange={(e) => handlerChange("dataAbertura", e.target.value)} className={classeInput} />
        </label>

        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Data de conclusão:
          <input name="dataConclusao" type="datetime-local" value={dataParaCampo(ordemServico.dataConclusao)} onChange={(e) => handlerChange("dataConclusao", e.target.value)} className={classeInput} />
        </label>

        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Descrição:
          <textarea name="descricao" value={ordemServico.descricao} required onChange={(e) => handlerChange("descricao", e.target.value)} placeholder="Descreva o serviço realizado..." rows={4} className={classeInput} />
        </label>

        <div className="border-t border-neutral-800 pt-5 flex flex-col gap-4">
          <h3 className="text-sm font-semibold text-white">Peças utilizadas (opcional)</h3>
          <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Peça:
            <select value={pecaId} onChange={(e) => setPecaId(e.target.value)} className={classeInput} disabled={carregando}>
              <option value="">Não adicionar peça agora</option>
              {pecas.map((item) => <option key={item.id} value={item.id ?? ""}>{item.nome} - estoque: {item.quantidade}</option>)}
            </select>
          </label>
          {pecaId && (
            <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Quantidade:
              <input type="number" min="1" step="1" required value={quantidadePeca} onChange={(e) => setQuantidadePeca(e.target.value)} className={classeInput} />
            </label>
          )}
          <p className="text-xs text-neutral-500">O sistema valida o estoque ao adicionar a peça e ao finalizar a ordem de serviço.</p>
        </div>

        <div className="flex items-center justify-end gap-4 mt-2">
          <Link href="/ordemServico" className="text-sm font-semibold text-neutral-400 hover:text-white">Cancelar</Link>
          <button type="submit" disabled={salvando || carregando} className="px-6 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 disabled:opacity-60 rounded-lg">
            {salvando ? "Salvando..." : "Salvar"}
          </button>
        </div>
      </div>
    </form>
  );
}
