"use client"

import { link } from "fs";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { type } from "os";

export default function Home() {

  const router = useRouter();

              const handlelogin = async()=> {
                  router.push("/login")
              }
    return (
     <>
      <div className="min-h-screen bg-slate-950 text-slate-100 font-sans antialiased selection:bg-amber-500 selection:text-slate-950">
        {/* HEADER */}
        <header className="sticky top-0 z-40 bg-slate-900/80 backdrop-blur-md border-b border-slate-800">
          <div className="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
  
            {/* LOGO AUTOFIX */}
            <div className="flex items-center gap-3 group cursor-default">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-amber-500 to-amber-300 flex items-center justify-center shadow-lg shadow-amber-500/20">
                <svg className="w-6 h-6 text-slate-950" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2.5}
                    d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 001.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"
                  />
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2.5}
                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                  />
                </svg>
              </div>
  
              <span className="text-xl font-black tracking-wider text-white">
                AUTO<span className="text-amber-400">FIX</span>
              </span>
            </div>
  
            <nav className="hidden md:flex items-center gap-8 text-sm font-medium text-slate-400">
              <span className="hover:text-amber-400 transition-colors cursor-default">
                Recursos
              </span>
              <span className="hover:text-amber-400 transition-colors cursor-default">
                Sobre
              </span>
              <span className="hover:text-amber-400 transition-colors cursor-default">
                Contato
              </span>
            </nav>
  
            
            <button
              type="button"
              className="inline-flex items-center justify-center px-5 py-2.5 text-sm font-bold text-slate-950 bg-amber-400 hover:bg-amber-300 rounded-xl transition-all shadow-lg shadow-amber-400/10 hover:shadow-amber-400/20 active:scale-95"
             onClick={handlelogin}
            >
              Entrar / Login
            </button>
          </div>
        </header>
  
        {/* CONTEÚDO PRINCIPAL */}
        <main>
  
          {/* HERO SECTION */}
          <section className="relative py-24 md:py-36 px-6 overflow-hidden">
            <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-amber-500/10 rounded-full blur-3xl pointer-events-none" />
  
            <div className="max-w-4xl mx-auto text-center space-y-8 relative z-10">
  
              <span className="inline-flex items-center gap-2 px-4 py-1.5 text-xs font-bold text-amber-400 bg-amber-400/10 border border-amber-400/20 rounded-full">
                <span className="w-2 h-2 rounded-full bg-amber-400 animate-pulse" />
                Gestão Inteligente para Oficina
              </span>
  
              <h1 className="text-4xl sm:text-7xl font-black text-white tracking-tight leading-tight">
                Acelere a gestão da sua oficina com{" "}
                <span className="text-transparent bg-clip-text bg-gradient-to-r from-amber-400 to-amber-200">
                  eficiência total
                </span>
              </h1>
  
              <p className="text-lg sm:text-xl text-slate-400 max-w-2xl mx-auto font-normal leading-relaxed">
                Agendamentos, controle de ordens de serviço, estoque de peças e diagnóstico mecânico centralizados em um único sistema.
              </p>
  
              <div className="pt-4 flex justify-center gap-4">
  
           
                <button
                  type="button"
                  className="inline-flex items-center gap-3 px-8 py-4 text-base font-bold text-slate-950 bg-gradient-to-r from-amber-400 to-amber-300 hover:from-amber-300 hover:to-amber-200 rounded-xl transition-all shadow-xl shadow-amber-400/15 transform hover:-translate-y-0.5 active:translate-y-0"
                >
                  Acessar Plataforma
  
                  <svg
                    className="w-5 h-5 text-slate-950"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2.5}
                      d="M14 5l7 7m0 0l-7 7m7-7H3"
                    />
                  </svg>
                </button>
  
              </div>
            </div>
          </section>
  
          {/* RECURSOS */}
          <section
            id="recursos"
            className="py-24 bg-slate-900/50 border-y border-slate-800/80 px-6 relative"
          >
            <div className="max-w-7xl mx-auto">
  
              <div className="text-center max-w-2xl mx-auto mb-16 space-y-3">
                <h2 className="text-3xl font-extrabold tracking-tight text-white sm:text-4xl">
                  Tudo o que sua mecânica precisa
                </h2>
  
                <p className="text-slate-400">
                  Tecnologia desenvolvida sob medida para simplificar o dia a dia da sua oficina.
                </p>
              </div>
  
              <div className="grid md:grid-cols-3 gap-8">
  
                {/* CARD 1 */}
                <div className="p-8 rounded-2xl bg-slate-900 border border-slate-800 hover:border-amber-400/30 transition-all hover:shadow-xl hover:shadow-amber-500/5 group">
  
                  <div className="w-12 h-12 bg-amber-400/10 rounded-xl flex items-center justify-center mb-6 text-amber-400 group-hover:scale-110 transition-transform">
                    <svg
                      className="w-6 h-6"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                      />
                    </svg>
                  </div>
  
                  <h3 className="text-xl font-bold text-white mb-2">
                    Ordens de Serviço Digitais
                  </h3>
  
                  <p className="text-slate-400 text-sm leading-relaxed">
                    Crie, acompanhe e envie orçamentos pelo WhatsApp em segundos sem complicação.
                  </p>
                </div>
  
                {/* CARD 2 */}
                <div className="p-8 rounded-2xl bg-slate-900 border border-slate-800 hover:border-amber-400/30 transition-all hover:shadow-xl hover:shadow-amber-500/5 group">
  
                  <div className="w-12 h-12 bg-amber-400/10 rounded-xl flex items-center justify-center mb-6 text-amber-400 group-hover:scale-110 transition-transform">
                    <svg
                      className="w-6 h-6"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"
                      />
                    </svg>
                  </div>
  
                  <h3 className="text-xl font-bold text-white mb-2">
                    Controle de Estoque de Peças
                  </h3>
  
                  <p className="text-slate-400 text-sm leading-relaxed">
                    Alertas automáticos de estoque baixo e histórico de aplicação de peças por veículo.
                  </p>
                </div>
  
                {/* CARD 3 */}
                <div className="p-8 rounded-2xl bg-slate-900 border border-slate-800 hover:border-amber-400/30 transition-all hover:shadow-xl hover:shadow-amber-500/5 group">
  
                  <div className="w-12 h-12 bg-amber-400/10 rounded-xl flex items-center justify-center mb-6 text-amber-400 group-hover:scale-110 transition-transform">
                    <svg
                      className="w-6 h-6"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                      />
                    </svg>
                  </div>
  
                  <h3 className="text-xl font-bold text-white mb-2">
                    Agendamento & Histórico
                  </h3>
  
                  <p className="text-slate-400 text-sm leading-relaxed">
                    Mantenha o histórico de manutenções de cada cliente com lembretes automáticos de revisão.
                  </p>
                </div>
  
              </div>
            </div>
          </section>
        </main>
  
        {/* FOOTER */}
        <footer className="py-8 px-6 border-t border-slate-800 bg-slate-950 text-center text-sm text-slate-500">
          <p>
            &copy; 2026 AutoFix Sistemas. Todos os direitos reservados.
          </p>
        </footer>
      </div>
     </>
    );
  }