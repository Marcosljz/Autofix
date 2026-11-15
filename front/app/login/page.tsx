
'use client'

import { useRouter } from "next/navigation";
import { login, definirToken } from "../redux/slices/authSlice";
import { buscarUsuarioLogado } from "../services/usuarioService";
import { useDispatch } from "react-redux";
import { loginBack } from "../services/authService";
import { AppDispatch } from "../redux/store";

export default function Login() {
    const router = useRouter();
    const dispatch = useDispatch<AppDispatch>();

    const handleLogin = async (formData: FormData) => {
        try {
            const emailTela = formData.get("email")?.toString() ?? "";
            const senhaTela = formData.get("senha")?.toString() ?? "";

            const loginResposta = await loginBack(emailTela, senhaTela);

            if (loginResposta != null) {

                // Guarda o token no Redux
                dispatch(definirToken(loginResposta.token));

                // Busca os dados do usuário utilizando o token
                const usuarioLogado = await buscarUsuarioLogado();

                // Guarda os dados completos no Redux
                dispatch(login({
                    token: loginResposta.token,
                    Usuario: { ...usuarioLogado }
                }));

                router.push("/home");

            } else {
                alert("Login ou senha inválido!");
            }

        } catch (error) {
            alert("Login ou senha inválido!");
        }
    };

    return (
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

                <form action={handleLogin} className="flex flex-col gap-5">

                    <div className="flex flex-col gap-2">
                        <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                            E-MAIL
                        </label>

                        <input
                            name="email"
                            type="email"
                            required
                            className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                        />
                    </div>

                    <div className="flex flex-col gap-2">
                        <label className="text-xs font-semibold text-neutral-400 tracking-wide">
                            Senha
                        </label>

                        <input
                            name="senha"
                            type="password"
                            required
                            className="w-full bg-neutral-800 border border-neutral-700 text-white placeholder-neutral-500 rounded-lg px-4 py-2.5 outline-none focus:border-orange-500 focus:ring-1 focus:ring-orange-500 transition-colors"
                        />
                    </div>

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
