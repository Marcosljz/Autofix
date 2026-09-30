import Link from "next/link";
import OrdemServicoForm from "../components/OrdemServicoForm";


export default function NovoOrdemServico() {

    return (

        <div className="h-full w-full px-8 py-8">

            <div className="max-w-3xl mx-auto">

                <div className="mb-6">

                    <Link
                        href="/ordemServico"
                        className="inline-flex items-center gap-1.5 text-xs font-semibold text-neutral-500 hover:text-orange-500 transition-colors"
                    >

                        <svg
                            className="w-3.5 h-3.5"
                            fill="none"
                            stroke="currentColor"
                            strokeWidth={2.5}
                            viewBox="0 0 24 24"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                d="M15 19l-7-7 7 7"
                            />
                        </svg>

                        Voltar para Listagem

                    </Link>


                    <div className="mt-3 flex items-center justify-between">

                        <div>

                            <h1 className="text-2xl font-bold text-white">
                                Nova Ordem de Serviço
                            </h1>

                            <p className="text-sm text-neutral-400 mt-1">
                                Preencha os dados para registrar uma nova ordem de serviço
                            </p>

                        </div>


                        <span className="hidden sm:inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-orange-500/10 text-orange-400 border border-orange-500/20">
                            Cadastro
                        </span>

                    </div>

                </div>


                <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-hidden">

                    <div className="px-6 py-4 border-b border-neutral-800">

                        <h2 className="text-sm font-semibold text-white">
                            Dados da ordem de serviço
                        </h2>

                    </div>


                    <div className="p-6">

                        <OrdemServicoForm />

                    </div>

                </div>

            </div>

        </div>

    )
}