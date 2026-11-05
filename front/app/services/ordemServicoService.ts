import { OrdemServico } from "../types/ordemservico";
import api from "./api";



export async function buscarListaOrdemServico() : Promise <OrdemServico[]> {

    const dados = await api.get<OrdemServico[]>("http://localhost:8080/ordemservicos");

    if (dados.status== 200 ){
        return dados.data;
    }

    
    return[];

}
export async function deletarOrdemServico(id:number):Promise<number> {

    var result = api.delete('http://localhost:8080/ordemservicos/' + id + '/excluir');
    return (await result).status;
}

export async function atualizarStatusOrdemServico(ordemservico:OrdemServico) :Promise<void>{
           let novoStatus = {};

        if (ordemservico.statusOrdemServico === "ABERTO") {

            novoStatus = {
                statusOrdemServico: "ANDAMENTO"
            };

        } else if (ordemservico.statusOrdemServico === "ANDAMENTO") {

            novoStatus = {
                statusOrdemServico: "FECHADO"
            };

        } else if (ordemservico.statusOrdemServico === "FECHADO") {

            novoStatus = {
                statusOrdemServico: "ABERTO"
            };

        } else if (ordemservico.statusOrdemServico === "EXCLUIDO") {

            novoStatus = {
                statusOrdemServico: "ABERTO"
            };

        }


        const dadosRetorno = await api.patch(
            "http://localhost:8080/ordemservicos/" +
            ordemservico.id +
            "/status",
            novoStatus
        );


        if (dadosRetorno.status == 200) {

            alert("Status atualizado com sucesso!");

        } else {

            alert(dadosRetorno.data);
            return;

        }
    
    }
export async function consultaOrdemServicoPorId(id:number):Promise<OrdemServico>{
    const valorOrdemServicoBack = await api.get<OrdemServico>('http://localhost:8080/ordemservicos/'+id);
    return valorOrdemServicoBack.data;

}

export async function  atualizarOrdemServico (ordemservico:OrdemServico): Promise<number>{
    var dadosRetorno = await  
    api.put<number>('http://localhost:8080/ordemservicos/'+ordemservico.id,ordemservico);
    return dadosRetorno.data;
}


export async function  salvarOrdemServico (ordemservico:OrdemServico): Promise<number>{
    var dadosRetorno = await  
    api.post<number>('http://localhost:8080/ordemservicos',ordemservico);
    return dadosRetorno.data;
}