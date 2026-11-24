import api from "./api";
import { Historico } from "../types/historico";

export async function buscarListaHistorico(): Promise<Historico[]> {

    const resposta = await api.get<Historico[]>("/historico");

    return resposta.data;
}