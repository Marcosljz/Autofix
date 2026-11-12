"use client";

import { atualizarVeiculo, salvarVeiculo } from "@/app/services/veiculoService";
import { veiculo } from "@/app/types/veiculo";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface VeiculoFormProps {
  veiculoExistente?: veiculo | null;
}

export default function VeiculoForm({ veiculoExistente }: VeiculoFormProps) {
  const router = useRouter();
  const [veiculoDados, setVeiculoDados] = useState<veiculo>(
    veiculoExistente || new veiculo(null, "", "", "", "", 0, "ATIVO"),
  );
  const [salvando, setSalvando] = useState(false);

  const handlerChange = (campo: "placa" | "modelo" | "ano" | "cor" | "quilometragem", valor: string) => {
    setVeiculoDados((anterior) => new veiculo(
      anterior.id,
      campo === "placa" ? valor : anterior.placa,
      campo === "modelo" ? valor : anterior.modelo,
      campo === "ano" ? valor : anterior.ano,
      campo === "cor" ? valor : anterior.cor,
      campo === "quilometragem" ? Number(valor) : anterior.quilometragem,
      anterior.statusVeiculo,
    ));
  };

  const handlerSalvar = async () => {
    setSalvando(true);
    try {
      if (veiculoExistente) {
        await atualizarVeiculo(veiculoDados);
        alert("Veículo atualizado com sucesso!");
      } else {
        await salvarVeiculo(veiculoDados);
        alert("Veículo cadastrado com sucesso!");
      }
      router.push("/veiculo");
    } catch {
      alert("Não foi possível salvar o veículo. Verifique os dados e tente novamente.");
    } finally {
      setSalvando(false);
    }
  };

  const classeInput = "w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500";

  return (
    <form action={handlerSalvar} className="max-w-lg mx-auto bg-neutral-900 border border-neutral-800 rounded-2xl p-8">
      <div className="flex flex-col gap-5">
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Placa:
          <input name="placa" value={veiculoDados.placa} required onChange={(e) => handlerChange("placa", e.target.value)} placeholder="ABC-1234" className={classeInput} />
        </label>
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Modelo:
          <input name="modelo" value={veiculoDados.modelo} required onChange={(e) => handlerChange("modelo", e.target.value)} placeholder="Civic" className={classeInput} />
        </label>
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Ano:
          <input name="ano" value={veiculoDados.ano} required onChange={(e) => handlerChange("ano", e.target.value)} placeholder="2025" className={classeInput} />
        </label>
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Cor:
          <input name="cor" value={veiculoDados.cor} required onChange={(e) => handlerChange("cor", e.target.value)} placeholder="Preto" className={classeInput} />
        </label>
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Quilometragem:
          <input name="quilometragem" type="number" min="0" value={veiculoDados.quilometragem} required onChange={(e) => handlerChange("quilometragem", e.target.value)} placeholder="50000" className={classeInput} />
        </label>
        <div className="flex items-center justify-end gap-4 mt-2">
          <Link href="/veiculo" className="text-sm font-semibold text-neutral-400 hover:text-white">Cancelar</Link>
          <button disabled={salvando} type="submit" className="px-6 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 disabled:opacity-60 rounded-lg">{salvando ? "Salvando..." : "Salvar"}</button>
        </div>
      </div>
    </form>
  );
}
