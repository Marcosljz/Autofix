"use client";

import { atualizarPeca, salvarPeca } from "@/app/services/pecaService";
import { Peca } from "@/app/types/peca";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface PecaFormProps {
  pecaExistente?: Peca | null;
}

export default function PecaForm({ pecaExistente }: PecaFormProps) {
  const router = useRouter();
  const [peca, setPeca] = useState<Peca>(pecaExistente || new Peca(null, "", "", "", "", "", "DISPONIVEL"));
  const [salvando, setSalvando] = useState(false);

  const handlerChange = (campo: "nome" | "preco" | "marca" | "descricao" | "quantidade", valor: string) => {
    setPeca((anterior) => new Peca(
      anterior.id,
      campo === "nome" ? valor : anterior.nome,
      campo === "preco" ? valor : anterior.preco,
      campo === "marca" ? valor : anterior.marca,
      campo === "descricao" ? valor : anterior.descricao,
      campo === "quantidade" ? valor : anterior.quantidade,
      anterior.statuspeca,
    ));
  };

  const handlerSalvar = async () => {
    setSalvando(true);
    try {
      if (pecaExistente) {
        await atualizarPeca(peca);
        alert("Peça atualizada com sucesso!");
      } else {
        await salvarPeca(peca);
        alert("Peça cadastrada com sucesso!");
      }
      router.push("/peca");
    } catch {
      alert("Não foi possível salvar a peça. Verifique os dados e tente novamente.");
    } finally {
      setSalvando(false);
    }
  };

  const classeInput = "w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500";

  return (
    <form action={handlerSalvar} className="max-w-lg mx-auto bg-neutral-900 border border-neutral-800 rounded-2xl p-8">
      <div className="flex flex-col gap-5">
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Nome:
          <input name="nome" value={peca.nome} required onChange={(e) => handlerChange("nome", e.target.value)} placeholder="Nome da peça" className={classeInput} />
        </label>
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Preço:
          <input name="preco" type="number" min="0" step="0.01" value={peca.preco} required onChange={(e) => handlerChange("preco", e.target.value)} placeholder="35.90" className={classeInput} />
        </label>
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Marca:
          <input name="marca" value={peca.marca} required onChange={(e) => handlerChange("marca", e.target.value)} placeholder="Bosch" className={classeInput} />
        </label>
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Quantidade:
          <input name="quantidade" type="number" min="0" value={peca.quantidade} required onChange={(e) => handlerChange("quantidade", e.target.value)} placeholder="10" className={classeInput} />
        </label>
        <label className="flex flex-col gap-2 text-xs font-semibold text-neutral-400">Descrição:
          <textarea name="descricao" value={peca.descricao} required onChange={(e) => handlerChange("descricao", e.target.value)} placeholder="Descrição da peça" rows={4} className={classeInput} />
        </label>
        <div className="flex items-center justify-end gap-4 mt-2">
          <Link href="/peca" className="text-sm font-semibold text-neutral-400 hover:text-white">Cancelar</Link>
          <button disabled={salvando} type="submit" className="px-6 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 disabled:opacity-60 rounded-lg">{salvando ? "Salvando..." : "Salvar"}</button>
        </div>
      </div>
    </form>
  );
}
