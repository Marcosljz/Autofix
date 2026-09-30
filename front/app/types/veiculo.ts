export class veiculo{
    constructor(
        public id: number | null,
        public placa:string,
        public modelo:string,
        public ano:string,
        public cor:string,
        public quilometragem:number,
        public  statusVeiculo:string
    ){}
}

export interface VeiculoFormProps {
    veiculoExistente?: veiculo
}

