"use client";

import { useEffect } from "react";
import { useSelector } from "react-redux";
import { useRouter } from "next/navigation";
import Footer from "../components/Footer";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import type { RootState } from "../redux/store";

export default function SistemaLayout({ children }: { children: React.ReactNode }) {
  const usuario = useSelector((state: RootState) => state.auth.usuario);
  const token = useSelector((state: RootState) => state.auth.token);
  const router = useRouter();

  useEffect(() => {
    if (!token || !usuario) router.replace("/login");
  }, [token, usuario, router]);

  if (!token || !usuario) {
    return <div className="min-h-screen bg-black flex items-center justify-center text-neutral-300">Verificando autenticação...</div>;
  }

  return (
    <div className="flex min-h-screen bg-black">
      <Sidebar />
      <div className="flex-1 flex flex-col min-w-0">
        <Header />
        <main className="flex-1 p-6">{children}</main>
        <Footer />
      </div>
    </div>
  );
}
