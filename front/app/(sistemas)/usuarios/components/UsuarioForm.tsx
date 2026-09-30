'use client'
import { Usuario, UsuarioFormProps } from "@/app/types/usuario";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";


export default function UsuarioForm({ usuarioExistente }: UsuarioFormProps) {
    /*() estado inicial*/
    const router = useRouter();
    // Se recebeu um usuarioExistente (modo edição), começa com ele já preenchido.
    // Se não recebeu (modo criação), começa com um Usuario "vazio" com status ATIVO.
    const [usuario, setUsuario] = useState<Usuario>(
        usuarioExistente || new Usuario(null, "", "", "ATIVO", "", "")
    );


    // Função genérica de troca de campo: recebe qual campo mudou e o novo valor,
    // e recria o objeto Usuario mantendo os campos que não mudaram (imutabilidade do estado do React).
    const handlerChange = (campo: 'nome' | 'email' | 'cpf' | 'senha', valor: string) => {

        setUsuario(valorAnterior =>
            new Usuario(
                valorAnterior.id,
                campo === 'nome' ? valor : valorAnterior.nome,
                campo === 'email' ? valor : valorAnterior.email,
                valorAnterior.status,
                campo === 'cpf' ? valor : valorAnterior.cpf,
                campo === 'senha' ? valor : valorAnterior.senha,

            )
        )
    }

    // Chamada quando o formulário é enviado. Decide entre PUT (editar) ou POST (criar)
    // dependendo se já existia um usuarioExistente.
    const handlerSalvar = async (formData: FormData) => {

        if (usuarioExistente) {
            var dadosRetorno = await axios.put<number>('http://localhost:8080/usuarios/' + usuario.id, usuario)


            if (dadosRetorno.status = 200) {
                alert("Usuário foi salvo com sucesso!");
            } else {
                alert(dadosRetorno.data);

                return;
            }


        } else {
            var dadosRetorno = await axios.post<number>('http://localhost:8080/usuarios', usuario)

            if (dadosRetorno.status = 200) {
                alert("Usuário foi salvo com sucesso!");
            } else {
                alert(dadosRetorno.data);

                return;
            }
        }
        router.push("/usuarios"); // depois de salvar, volta pra listagem

    }

    return (
        <form action={handlerSalvar} className="max-w-lg mx-auto bg-neutral-900 border border-neutral-800 rounded-2xl p-8">
            <div className="flex flex-col gap-5">
                <div>
                    <div className="flex flex-col gap-2">
                        <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                            Nome Completo:
                        </label>
                        <input
                            name="nome"
                            value={usuario.nome}
                            required
                            onChange={(e) => handlerChange('nome', e.target.value)}
                            placeholder="João da silva Sauro"
                            className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors">
                        </input>
                    </div>
                </div>
                <div className="flex flex-col gap-2">
                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Cpf:
                    </label>
                    <input
                        name="CPF"
                        value={usuario.cpf}
                        onChange={(e) => handlerChange('cpf', e.target.value)}
                        placeholder="000.000.000-00"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors">
                    </input>
                </div>
                <div className="flex flex-col gap-2">
                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Email:
                    </label>
                    <input
                        name="email"
                        value={usuario.email}
                        onChange={(e) => handlerChange('email', e.target.value)}
                        placeholder="João@gmail.com.br"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors">
                    </input>
                </div>
                <div className="flex flex-col gap-2">
                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Senha:
                    </label>
                    <input
                        name="senha"
                        value={usuario.senha}
                        required
                        onChange={(e) => handlerChange('senha', e.target.value)}
                        placeholder="******************"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors">
                    </input>
                </div>

                <div className="flex items-center justify-end gap-4 mt-2">
                    <Link href={"/usuarios"} className="text-sm font-semibold text-neutral-400 hover:text-white transition-colors">Cancelar</Link>
                    <button type="submit" className="px-6 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-lg transition-colors active:scale-95">Salvar</button>
                </div>
            </div>
        </form>
    );
}