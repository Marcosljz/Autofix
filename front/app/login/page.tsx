'use client'

// Axios é usado para fazer a requisição do login para o backend
import axios from "axios";

// Hook usado para navegar para outra página depois do login
import { useRouter } from "next/navigation";

// Tipo que representa a resposta esperada do login
import { LoginResponse } from "../types/auth";


// Componente responsável pela tela de login
export default function Login(){

    // Permite fazer navegação entre as páginas pelo código
    const router = useRouter();


    // Função executada quando o formulário de login é enviado
    const handlelogin = async(formData:FormData)=> {

        try{

            // Permite pausar a execução aqui durante a depuração no navegador
            debugger

            // Pega o valor do campo de e-mail enviado pelo formulário
            const emailTela = formData.get("email")?.toString() ?? "";

            // Pega o valor do campo de senha enviado pelo formulário
            const senhaTela = formData.get("senha")?.toString() ?? "";


            // Envia e-mail e senha para o endpoint de login do backend
            var loginResposta = await axios.post<LoginResponse>(
                "http://localhost:8080/login",
                {
                    email: emailTela,
                    senha: senhaTela
                }
            );


            // Se o backend retornar status 200, o login foi realizado
            if(loginResposta.status==200){

                // Redireciona o usuário para a página principal do sistema
                router.push("/home")

            }else{

                // Caso o backend retorne outro status
                alert("Login ou senha Invalido!")
            }

        }catch(error){

            // Caso aconteça algum erro na requisição ou no login
            alert("Login ou senha Invalido!")

        }

    }


    return(

    <div className="min-h-screen bg-black flex items-center justify-center px-4">

        <div className="w-full max-w-sm bg-neutral-900 border border-neutral-800 rounded-2xl p-8 shadow-xl shadow-orange-500/5">

            <div>

                <h1 className="text-2xl font-bold text-white text-center mb-8">
                    Entrar no Sistema
                </h1>

                <p className="text-sm text-slate-400">
                    Insira suas credenciais para acessar o painel
                </p>

            </div>


            {/* Formulário responsável por receber as credenciais */}
            <form action={handlelogin} className="flex flex-col gap-5">


                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        E-MAIL
                    </label>


                    {/* Campo onde o usuário informa o e-mail */}
                    <input
                        name="email"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors">
                    </input>

                </div>


                <div className="flex flex-col gap-2">

                    <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                        Senha
                    </label>


                    {/* Campo onde o usuário informa a senha */}
                    <input
                        name="senha"
                        className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors">
                    </input>

                </div>


                {/* Envia o formulário e executa handlelogin */}
                <button
                    type="submit"
                    className="mt-2 w-full bg-orange-500 hover:bg-orange-400 text-black font-bold py-2.5 rounded-lg transition-colors active:scale-95"
                >
                    Entrar
                </button>

            </form>

        </div>

    </div>

   );
}