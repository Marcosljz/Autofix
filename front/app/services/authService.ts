import { LoginResponse } from "../types/auth";
import api from "./api";

export async function loginBack(email:string,senha:string) :Promise<LoginResponse>{

     var loginResposta = await api.post<LoginResponse>("/login",
            {email:email,senha:senha});
  if(loginResposta.status==200){
    alert("login ou senha Invalido!")
  }

       return loginResposta.data
}