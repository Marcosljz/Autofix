// Componentes que serão exibidos em todas as páginas do sistema
import Footer from "../components/Footer";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";


// Layout principal do sistema
// O children representa o conteúdo da página que está sendo acessada
export default function SistemaLayout({children}){
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