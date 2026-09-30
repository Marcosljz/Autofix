"use client"
// "use client" -> diretiva do Next.js dizendo que esse componente roda no navegador (não no servidor),
// necessário porque ele usa hooks como useState/useEffect e interage com o usuário.
import Link from "next/link";
import UsuarioForm from "../../components/UsuarioForm";
import { useParams, useRouter, useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import { Usuario } from "@/app/types/usuario";
import axios from "axios";



export default function EditarUsuario(){
    const router=useRouter(); // permite navegar pra outras rotas via código (ex: router.push)

    const parametro = useParams(); // pega os parâmetros da URL (ex: /usuarios/editar/5 -> { codigo: "5" })

    const codigo = Number(parametro.codigo); // converte o parâmetro (que vem como string) pra número

    // Estado que guarda o usuário buscado no back-end. Começa como null (ainda não carregou).
    const [usuario,setUsuario] = useState<Usuario|null>(null)


    // useEffect com array de dependências vazio [] -> roda só UMA vez, quando o componente monta na tela.
    useEffect(()=>{

        buscarDados();


    },[])

    // Busca os dados do usuário no back-end pelo id (codigo) vindo da URL.
    const buscarDados = async() =>{

        const valoUsuarioBack= await axios.get<Usuario>('http://localhost:8080/usuarios/'+codigo);
        if(valoUsuarioBack.status==200){
            setUsuario(valoUsuarioBack.data); // deu certo -> guarda o usuário no estado
        }else{
              router.push("/usuarios") // deu errado -> volta pra listagem

        }

      
    }

    {/* Enquanto o usuário ainda não foi carregado, mostra "carregando" e nem renderiza o form */}
    if(!usuario) return(<div className="p-8"> carregando Ddados ...</div>)

    return(
        <div className="h-full w-full px-8 py-8">
            <div className="max-w-3xl mx-auto">
                <div className="mb-6">
                    <Link href="/usuarios" className="inline-flex items-center gap-1.5 text-xs font-semibold text-neutral-500 hover:text-orange-500 transition-colors">
                    <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" strokeWidth={2.5} viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" d="M15 19l-7-7 7-7" />
                    </svg>
                    Voltar para Listagem
                    </Link>
                    <div className="mt-3 flex items-center justify-between">
                        <div>
                            <h1 className="text-2xl font-bold text-white">Editar usuario {codigo}</h1>
                            <p className="text-sm text-neutral-400 mt-1">Preencha os dados para registrar um novo usuário</p>
                        </div>
                        <span className="hidden sm:inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-orange-500/10 text-orange-400 border border-orange-500/20">
                            Cadastro
                        </span>
                    </div>
                </div>
                <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-hidden">
                    <div className="px-6 py-4 border-b border-neutral-800">
                        <h2 className="text-sm font-semibold text-white">Dados do usuário</h2>
                    </div>
                    <div className="p-6">
                        {/* Passa o usuário já carregado pro form, que provavelmente pré-preenche os campos com esses dados */}
                        <UsuarioForm usuarioExistente={usuario}/>
                    </div>
                </div>
            </div>
        </div>
    )
}