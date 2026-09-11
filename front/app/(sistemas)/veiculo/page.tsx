import Link from "next/link";

export default function Veiculos() {
    return (
        <div className="min-h-screen bg-black px-6 py-10">
            <div className="max-w-5xl mx-auto flex items-center justify-between mb-8">
                <h1 className="text-2xl font-bold text-white">
                    Veículos
                </h1>

                <Link href="/veiculos/novo" className="inline-flex items-center justify-center px-5 py-2.5 text-sm font-bold text-black bg-orange-500 hover:bg-orange-400 rounded-xl transition-colors active:scale-95">
                    Novo veículo
                </Link>
            </div>

            <div className="max-w-5xl mx-auto">
                <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-hidden">
                    <table className="w-full text-left">
                        <thead className="bg-neutral-800/50">
                            <tr>
                                <th className="px-6 py-3 text-xs font-semibold text-neutral-400 uppercase tracking-wide">veículo</th>
                            </tr>
                        </thead>

                        <tbody className="divide-y divide-neutral-800">
                            <tr className="hover:bg-neutral-800/40 transition-colors">
                                <td className="px-6 py-4 text-sm text-white">gol quadrado</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
