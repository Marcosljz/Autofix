"use client"

// Hook do Next.js usado para navegar entre páginas pelo código
import { useRouter } from "next/navigation";


// Componente responsável pelo cabeçalho do sistema
export default function Header() {

    // Cria o router para poder mudar de página através do código
    const router = useRouter();


    // Função executada quando o usuário clica em "Sair"
    const handleSair = () => {

        // Pergunta ao usuário se realmente deseja sair
        const confirmar = window.confirm("Tem certeza que quer sair?");

        // Se confirmar, volta para a página inicial
        if (confirmar) {
            router.push("/");
        }
    }


    return (
        <header className="bg-neutral-900 border-b border-neutral-800 px-6 py-4">

            <div className="flex items-center justify-between">

                <div className="flex items-center gap-3">

                    {/* Ícone que representa o usuário */}
                    <div className="w-9 h-9 rounded-full bg-orange-500/10 border border-orange-500/20 flex items-center justify-center text-orange-500">

                        <svg xmlns="http://www.w3.org/2000/svg"
                            className="w-5 h-5"
                            fill="none"
                            stroke="currentColor"
                            strokeWidth={2}
                            viewBox="0 0 24 24">

                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                d="M19 21v-2a4 4 0 00-4-4H9a4 4 0 00-4 4v2"
                            />

                            <circle
                                cx="12"
                                cy="7"
                                r="4"
                                strokeLinecap="round"
                                strokeLinejoin="round"
                            />

                        </svg>

                    </div>

                    <span className="text-sm font-semibold text-white">
                        Usuário Marcos Leão
                    </span>

                </div>


                {/* Botão que chama a função de sair */}
                <button
                    onClick={handleSair}
                    className="px-4 py-2 text-sm font-bold text-orange-500 border border-orange-500/30 rounded-lg hover:bg-orange-500 hover:text-black transition-colors active:scale-95"
                >
                    Sair
                </button>

            </div>

        </header>
    );
}