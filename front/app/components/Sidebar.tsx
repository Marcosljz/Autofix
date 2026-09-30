// Componente Link do Next.js usado para navegar entre as páginas
import Link from "next/link";


// Componente responsável pelo menu lateral do sistema
export default function Sidebar(){

    return(

        // Estrutura principal do menu lateral
        <aside className="w-64 h-screen bg-neutral-900 border-r border-neutral-800 flex flex-col">

            {/* Nome/logo do sistema */}
            <div className="px-6 py-5 text-lg font-black text-white border-b border-neutral-800">
                AUTO<span className="text-orange-500">FIX</span>2026
            </div>


            {/* Área que contém os links de navegação */}
            <nav className="flex flex-col gap-1 p-4">

                {/* Cada Link leva para uma página diferente do sistema */}
                <Link
                    href="/home"
                    className="px-4 py-2.5 rounded-lg text-sm font-medium text-neutral-400 hover:bg-neutral-800 hover:text-white transition-colors"
                >
                    Home
                </Link>

                <Link
                    href="/usuarios"
                    className="px-4 py-2.5 rounded-lg text-sm font-medium text-neutral-400 hover:bg-neutral-800 hover:text-white transition-colors"
                >
                    Usuários
                </Link>

                <Link
                    href="/veiculo"
                    className="px-4 py-2.5 rounded-lg text-sm font-medium text-neutral-400 hover:bg-neutral-800 hover:text-white transition-colors"
                >
                    veiculo
                </Link>

                <Link
                    href="/peca"
                    className="px-4 py-2.5 rounded-lg text-sm font-medium text-neutral-400 hover:bg-neutral-800 hover:text-white transition-colors"
                >
                    peça
                </Link>

                <Link
                    href="/ordemServico"
                    className="px-4 py-2.5 rounded-lg text-sm font-medium text-neutral-400 hover:bg-neutral-800 hover:text-white transition-colors"
                >
                    Ordem de Serviço
                </Link>

            </nav>

        </aside>
    );
}