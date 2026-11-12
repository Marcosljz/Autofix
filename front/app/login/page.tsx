"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useDispatch } from "react-redux";
import type { AppDispatch } from "../redux/store";
import { login, logout, setToken } from "../redux/slices/authSlice";
import { buscarUsuarioLogado } from "../services/usuarioService";
import { loginBack } from "../services/authService";

export default function Login() {
  const router = useRouter();
  const dispatch = useDispatch<AppDispatch>();
  const [carregando, setCarregando] = useState(false);

  const handleLogin = async (formData: FormData) => {
    const email = formData.get("email")?.toString().trim() ?? "";
    const senha = formData.get("senha")?.toString() ?? "";

    if (!email || !senha) {
      alert("Informe o e-mail e a senha.");
      return;
    }

    setCarregando(true);
    try {
      const resposta = await loginBack(email, senha);
      dispatch(setToken(resposta.token));
      const usuario = await buscarUsuarioLogado();
      dispatch(login({ token: resposta.token, Usuario: usuario }));
      router.push("/home");
    } catch {
      dispatch(logout());
      alert("Não foi possível entrar. Verifique seu e-mail e sua senha.");
    } finally {
      setCarregando(false);
    }
  };

  return (
    <div className="min-h-screen bg-black flex items-center justify-center px-4">
      <div className="w-full max-w-sm bg-neutral-900 border border-neutral-800 rounded-2xl p-8 shadow-xl shadow-orange-500/5">
        <h1 className="text-2xl font-bold text-white text-center mb-3">Entrar no Sistema</h1>
        <p className="text-sm text-slate-400 mb-8">Insira suas credenciais para acessar o painel</p>
        <form action={handleLogin} className="flex flex-col gap-5">
          <div className="flex flex-col gap-2">
            <label htmlFor="email" className="text-xs font-semibold text-neutral-400 tracking-wide">E-MAIL</label>
            <input id="email" name="email" type="email" autoComplete="username" required className="w-full bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5 outline-none focus:border-orange-500" />
          </div>
          <div className="flex flex-col gap-2">
            <label htmlFor="senha" className="text-xs font-semibold text-neutral-400 tracking-wide">SENHA</label>
            <input id="senha" name="senha" type="password" autoComplete="current-password" required className="w-full bg-neutral-800 border border-neutral-700 text-white rounded-lg px-4 py-2.5 outline-none focus:border-orange-500" />
          </div>
          <button disabled={carregando} type="submit" className="mt-2 w-full bg-orange-500 hover:bg-orange-400 disabled:opacity-60 text-black font-bold py-2.5 rounded-lg transition-colors">
            {carregando ? "Entrando..." : "Entrar"}
          </button>
        </form>
      </div>
    </div>
  );
}
