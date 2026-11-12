import { LoginResponse } from "../types/auth";
import api from "./api";

export async function loginBack(email: string, senha: string): Promise<LoginResponse> {
  const resposta = await api.post<LoginResponse>("/login", { email, senha });
  return resposta.data;
}
