'use client'
import { useSelector } from "react-redux";
import Footer from "../components/Footer";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import { RootState } from "../redux/store";
import { useRouter } from "next/navigation";
import { useEffect } from "react";


// Layout principal do sistema
// O children representa o conteúdo da página que está sendo acessada
export default function SistemaLayout({children}){

const usuario= useSelector((state:RootState)=> state.auth.usuario);

const router = useRouter();

useEffect(()=>{
  if(usuario == null){
    router.push('/login');
  }
})

  return (
    <div className="flex min-h-screen bg-black">

      {/* Menu lateral do sistema */}
      <Sidebar/>

      <div className="flex-1 flex flex-col">

        {/* Cabeçalho do sistema */}
        <Header/>

        {/* Aqui é exibido o conteúdo da página atual */}
        <main className="flex-1 p-6">
          {children} 
        </main>

        {/* Rodapé do sistema */}
        <Footer/>

      </div>
    
    </div>
  )
}