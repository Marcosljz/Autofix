import { AuthState } from "@/app/types/auth";
import { Usuario } from "@/app/types/usuario";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { stringify } from "querystring";
import Cookies from "js-cookie";
import { json } from "stream/consumers";

const usuarioRecover = Cookies.get('usario')
const tokenRecover = Cookies.get('token')


const initialState: AuthState = {
    usuario:usuarioRecover? JSON.parse(usuarioRecover) as Usuario:null,
    token: tokenRecover ?? ""
}


const authSlice = createSlice({
    name :'auth',
    initialState,
    reducers:{
        login: ( state ,action: PayloadAction<{token:string, Usuario:Usuario}>) =>{
              Cookies.set('token',action.payload.token,{secure: true, expires: 7});
              Cookies.set('usuario', JSON.stringify(action.payload.Usuario),{secure: true, expires: 7})

            state.token = action.payload.token;
             state.usuario = action.payload.Usuario;


        },
        logout: ( state ) =>{
            Cookies.remove('token')
              Cookies.remove('usuario')

              state.token="";
              state.usuario = null;
        }
    } //funções da classe//
})

export const { login, logout } = authSlice.actions;

export default authSlice.reducer;

