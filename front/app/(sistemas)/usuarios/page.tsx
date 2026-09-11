"use client"
import { Usuario } from "@/app/types/usuario";
import axios from "axios";
import Link from "next/link";
import { useEffect, useState } from "react";


export default function Usuarios() {

    const [usuarios, setUsuarios] = useState<Usuario[]>([]);
 
    useEffect(() => {
        carregarDados();
    }, []);
 
    const carregarDados = async () =>{
 
        try{
 
        const dados = await axios.get<Usuario[]>("http://localhost:8080/auth/login");
 
 
        setUsuarios( dados.data);
 
    } catch(error) {
        alert("Erro ao carregar dados do servidor!")
    }
 

}
 
    return(
 
        <div className="min-h-screen bg-black px-6 py-10">
            <div className="max-w-5xl mx-auto flex items-center justify-between mb-8">
                <h1 className="text-2xl font-bold text-white">
                    Usuarios
                </h1>

                <Link href="/usuarios/novo" className="inline-flex items-center justify-center px-5 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-xl transition-colors active:scale-95">
                    Novo usuario
                </Link>
            </div>

            <div className="max-w-5xl mx-auto">
                <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-hidden">
                    <table className="w-full text-left">
                        <thead className="bg-neutral-800/50">
                            <tr>
                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">nome</th>
                            </tr>
                        </thead>

                        <tbody className="divide-y divide-slate-800 text-slate-200">
                       
                       {usuarios.map((usuario) =>(
                       <tr key={usuario.id}className="hover:bg-slate-800/40 transition-colors">
                           <td className="px-6 py-4 text-sm text-white">
                               {usuario.id}
                           </td>
                       <td className="px-6 py-4 text-sm text-white">
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
                       </tr>
                       )) }

                       {usuarios.length === 0 &&
                       (
                           <tr>
                               <td colSpan={5} className="px-6 py-12 text-center font-medium text-slate-100 text-center">
                                   Nenhum usuário encontrado.
                               </td>
                           </tr>
                       ) }
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

