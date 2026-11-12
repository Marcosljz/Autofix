"use client";

import { useRouter } from "next/navigation";
import { useDispatch, useSelector } from "react-redux";
import type { AppDispatch, RootState } from "../redux/store";
import { logout } from "../redux/slices/authSlice";

export default function Header() {
  const usuario = useSelector((state: RootState) => state.auth.usuario);
  const dispatch = useDispatch<AppDispatch>();
  const router = useRouter();

  const handleSair = () => {
    if (window.confirm("Tem certeza que quer sair?")) {
      dispatch(logout());
      router.replace("/login");
    }
  };

  return (
    <header className="bg-neutral-900 border-b border-neutral-800 px-6 py-4">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-full bg-orange-500/10 border border-orange-500/20 flex items-center justify-center text-orange-500">
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" stroke="currentColor" strokeWidth={2} viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" d="M19 21v-2a4 4 0 00-4-4H9a4 4 0 00-4 4v2" />
              <circle cx="12" cy="7" r="4" />
            </svg>
          </div>
          <span className="text-sm font-semibold text-white">
            {usuario?.nome?.toLocaleUpperCase() || "Usuário não identificado"}
          </span>
        </div>
        <button type="button" onClick={handleSair} className="px-4 py-2 text-sm font-bold text-orange-500 border border-orange-500/30 rounded-lg hover:bg-orange-500 hover:text-black transition-colors">
          Sair
        </button>
      </div>
    </header>
  );
}
