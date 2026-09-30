"use client"

// Modelo/tipo usado para representar um usuário
import { Usuario } from "@/app/types/usuario";

// Axios é usado para fazer as requisições para o backend
import axios from "axios";

// Link permite navegar entre páginas do Next.js
import Link from "next/link";

// Hooks usados para controlar estado e executar código quando a página carrega
import { useEffect, useState } from "react";


// Página responsável por listar os usuários cadastrados
export default function Usuarios() {

    // Guarda a lista de usuários que será exibida na tabela
    const [usuarios, setUsuarios] = useState<Usuario[]>([]);

    // Executa uma vez quando a página é carregada
    useEffect(() => {
        carregarDados();
    }, []);


    // Busca os usuários no backend
    const carregarDados = async () => {

        try {

            // Faz uma requisição GET para buscar todos os usuários
            const dados = await axios.get<Usuario[]>("http://localhost:8080/usuarios");


            // Coloca os usuários recebidos do backend no estado
            setUsuarios(dados.data);

        } catch (error) {

            // Mostra uma mensagem caso não consiga acessar o servidor
            alert("Erro ao carregar dados do servidor!")
        }
    }


    // Função responsável por excluir um usuário
    const handleDeletarUsuario = async (usuario: Usuario) => {

        // Envia uma requisição DELETE para o backend
        const dadosRetorno = await axios.delete(
            'http://localhost:8080/usuarios/' + usuario.id + '/excluir'
        )


        // Verifica se a requisição foi concluída com sucesso
        if (dadosRetorno.status = 200) {

            alert("excluido com sucesso");

        } else {

            alert(dadosRetorno.data);

            return;
        }

        // Atualiza a tabela depois da exclusão
        carregarDados();
    }


    // Função responsável por alterar o status do usuário
    const handleAlterarStatusUsuario = async (usuario: Usuario) => {


        // Objeto que será enviado para o backend
        let novoStatus = {};

        // Se estiver ativo, muda para bloqueado
        if (usuario.status === "ATIVO") {
            novoStatus = { status: "BLOQUEADO" }

        } else {

            // Caso contrário, volta para ativo
            novoStatus = { status: "ATIVO" }
        }


        // Envia o novo status para o backend através de PATCH
        const dadosRetorno = await
            axios.patch(
                'http://localhost:8080/usuarios/' + usuario.id + '/status',
                novoStatus
            );


        // Verifica se a alteração foi realizada
        if (dadosRetorno.status == 200) {

            alert("Atulizado status com sucesso!");

        } else {

            alert(dadosRetorno.data);

            return;
        }


        // Atualiza a lista para mostrar o novo status
        carregarDados();

    }


    return (

        <div className="min-h-screen bg-black px-6 py-10">

            <div className="max-w-5xl mx-auto flex items-center justify-between mb-8">

                <h1 className="text-2xl font-bold text-white">
                    Usuarios
                </h1>


                {/* Leva para a página de cadastro de um novo usuário */}
                <Link
                    href="/usuarios/novo"
                    className="inline-flex items-center justify-center px-5 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-xl transition-colors active:scale-95"
                >
                    Novo usuario
                </Link>

            </div>


            <div className="max-w-5xl mx-auto">

                <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-hidden">

                    <table className="w-full text-left">

                        <thead className="bg-neutral-800/50">
                            <tr>

                                {/* Cabeçalho das colunas da tabela */}
                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">ID</th>
                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">Nome</th>
                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">CPF</th>
                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">Email</th>
                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">Status</th>
                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">Ações</th>

                            </tr>
                        </thead>


                        <tbody className="divide-y divide-slate-800 text-slate-200">

                            {/* Percorre o array de usuários e cria uma linha para cada usuário */}
                            {usuarios.map((usuario) => (

                                <tr key={usuario.id} className="hover:bg-slate-800/40 transition-colors">

                                    <td className="px-6 py-4 text-sm text-white">
                                        {usuario.id}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white" >
                                        {usuario.nome}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {usuario.cpf}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {usuario.email}
                                    </td>

                                    <td className="px-6 py-4 text-sm text-white">
                                        {usuario.status}
                                    </td>


                                    <td className="px-6 py-4 text-sm font-medium">

                                        <div className="flex items-center justify-end gap-2">


                                            {/* Botão que leva para a página de edição do usuário */}
                                            <Link
                                                href={`/usuarios/${usuario.id}/editar`}
                                                className="group inline-flex items-center gap-2 px-3.5 py-2 text-xs font-semibold
                                                text-blue-400 bg-blue-500/10
                                                border border-blue-500/30
                                                hover:text-white hover:bg-blue-500
                                                hover:border-blue-400
                                                rounded-xl
                                                transition-all duration-300
                                                hover:-translate-y-0.5
                                                hover:shadow-lg hover:shadow-blue-500/20"
                                            >

                                                <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    fill="none"
                                                    viewBox="0 0 24 24"
                                                    strokeWidth={2}
                                                    stroke="currentColor"
                                                    className="w-3.5 h-3.5 transition-transform duration-300 group-hover:rotate-[-8deg] group-hover:scale-110"
                                                >
                                                    <path
                                                        strokeLinecap="round"
                                                        strokeLinejoin="round"
                                                        d="M16.862 3.487a2.25 2.25 0 0 1 3.182 3.182L8.25 18.463 4 19.5l1.037-4.25L16.862 3.487Z"
                                                    />
                                                </svg>

                                                Editar
                                            </Link>


                                            {/* Botão que chama a função de exclusão */}
                                            <button
                                                type="button"
                                                onClick={() => handleDeletarUsuario(usuario)}
                                                className="group inline-flex items-center gap-2 px-3.5 py-2 text-xs font-semibold
                                                text-orange-400 bg-orange-500/10
                                                border border-orange-500/30
                                                hover:text-white hover:bg-orange-500
                                                hover:border-orange-400
                                                rounded-xl
                                                transition-all duration-300
                                                hover:-translate-y-0.5
                                                hover:shadow-lg hover:shadow-orange-500/20"
                                            >

                                                <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    fill="none"
                                                    viewBox="0 0 24 24"
                                                    strokeWidth={2}
                                                    stroke="currentColor"
                                                    className="w-3.5 h-3.5 transition-transform duration-300 group-hover:scale-110"
                                                >
                                                    <path
                                                        strokeLinecap="round"
                                                        strokeLinejoin="round"
                                                        d="M6 7h12M9 7V5h6v2m-7 4v6m4-6v6m4-6v6M5 7l1 14h12l1-14"
                                                    />
                                                </svg>

                                                Deletar
                                            </button>


                                            {/* Botão que alterna entre ATIVO e BLOQUEADO */}
                                            <button
                                                type="button"
                                                onClick={() => handleAlterarStatusUsuario(usuario)}
                                                className={`group inline-flex items-center gap-2 px-3.5 py-2 text-xs font-semibold
                                                rounded-xl border transition-all duration-300
                                                hover:-translate-y-0.5
                                               ${usuario.status === "BLOQUEADO"
                                                        ? "text-yellow-400 bg-yellow-500/10 border-yellow-500/30 hover:text-white hover:bg-yellow-500 hover:border-yellow-400 hover:shadow-lg hover:shadow-yellow-500/20"
                                                        : "text-green-400 bg-green-500/10 border-green-500/30 hover:text-white hover:bg-green-500 hover:border-green-400 hover:shadow-lg hover:shadow-green-500/20"
                                                    }`}
                                            >
                                                {usuario.status}
                                            </button>


                                        </div>
                                    </td>

                                </tr>
                            ))}


                            {/* Mostra uma mensagem quando não existe nenhum usuário */}
                            {usuarios.length === 0 &&
                                (
                                    <tr>
                                        <td colSpan={6} className="px-6 py-12 text-center font-medium text-slate-100">
                                            Nenhum usuário encontrado.
                                        </td>
                                    </tr>
                                )}

                        </tbody>

                    </table>

                </div>
            </div>

        </div>
    );
}