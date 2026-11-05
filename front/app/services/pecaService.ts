import { Peca } from "../types/peca";
import api from "./api";

export async function buscarListaPeca() : Promise<Peca[]>{

   const dados = await api.get<Peca[]>('/peca');
   
    if (dados.status== 200 ){
        return dados.data;
    }

    return[];
}

export async function deletarPeca(id:number) : Promise<number>{

    var result = api.delete('/pecas/' + id + '/excluir');
    return (await result).status;
}

export async function atualizarStatusPecas(pecas:Peca):Promise<void>{
    var novoStatus = {};

       
        var novoStatus = {};

        if (pecas.statuspeca === "DISPONIVEL") {
            novoStatus = { statusPeca: "ESGOTADO" }
        } else {
            novoStatus = { statusPeca: "DISPONIVEL" }
        }

        var dadosRetorno = await api.patch(
            '/pecas/' + pecas.id + '/status',
            novoStatus
        );

        if (dadosRetorno.status === 200) {
            alert("Atualizado status com sucesso!");
        } else {
            alert(dadosRetorno.data);
            return;
        }
}

export async function consultaPecaPorId(id:number):Promise<Peca>{
    const valorPecaBack = await api.get<Peca>('/pecas/'+id);
    return valorPecaBack.data;

}


export async function  atualizarPeca (pecas:Peca): Promise<number>{
    var dadosRetorno = await  
    api.put<number>('/pecas/'+pecas.id,pecas);
    return dadosRetorno.data;
}


export async function  salvarPeca (pecas:Peca): Promise<number>{
    var dadosRetorno = await  
    api.post<number>('/pecas',pecas);
    return dadosRetorno.data;
}