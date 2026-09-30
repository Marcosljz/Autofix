// Componente responsável pelo rodapé do sistema
export default function Footer(){

    // Pega automaticamente o ano atual do computador
    const anoatual = new Date().getFullYear();

    return(

        // Estrutura visual do rodapé
        <footer className="w-full bg-neutral-900 border-t border-neutral-800 px-6 py-4">

            <div className="w-full flex items-center justify-center">

                <div className="text-xs text-neutral-500 flex items-center gap-1 whitespace-nowrap">

                    {/* Exibe o símbolo de copyright e o ano atual */}
                    <p>&copy;{anoatual}</p>

                    {/* Nome exibido no rodapé */}
                    <span className="text-orange-500 font-semibold">
                        Marcos.
                    </span>

                    <span>
                        Todos os direitos reservados
                    </span>

                </div>

            </div>

        </footer>
    );
}