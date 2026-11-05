import { veiculo } from "../types/veiculo";
import api from "./api";

export async function buscarListaVeiculos() : Promise<veiculo[]>{

   const dados = await api.get<veiculo[]>( "http://localhost:8080/veiculos");

   
    if (dados.status= 200 ){
        return dados.data;
    }

    
    return[];
}

export async function deletarVeiculo(id:number) : Promise<number>{

    var result = api.delete('http://localhost:8080/veiculos/' + id + '/excluir');
    return (await result).status;
}

export async function atualizarStatusVeiculo(veiculo:veiculo):Promise<void>{
    var novoStatus = {};

        if (veiculo.statusVeiculo === "ATIVO") {

            novoStatus = {
                statusVeiculo: "INATIVO"
            };

        } else {

            novoStatus = {
                statusVeiculo: "ATIVO"
            };

        }


        var dadosRetorno = await api.patch(
            'http://localhost:8080/veiculos/' + veiculo.id + '/status',
            novoStatus
        );


        if (dadosRetorno.status == 200) {

            alert("Atualizado status com sucesso!");

        } else {

            alert(dadosRetorno.data);
            return;

        }
}

export async function consultaVeiculoPorId(id:number):Promise<veiculo>{
    const valorVeiculoBack = await api.get<veiculo>('http://localhost:8080/veiculos/'+id);
    return valorVeiculoBack.data;

}


export async function  atualizarVeiculo (veiculo:veiculo): Promise<number>{
    var dadosRetorno = await  
    api.put<number>('http://localhost:8080/veiculos/'+veiculo.id,veiculo);
    return dadosRetorno.data;
}


export async function  salvarVeiculo (veiculo:veiculo): Promise<number>{
    var dadosRetorno = await  
    api.post<number>('http://localhost:8080/veiculos',veiculo);
    return dadosRetorno.data;
}