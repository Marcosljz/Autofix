export class OrdemServico{
    constructor(
        public id: number | null,
        public dataAbertura:Date,
        public dataConclusao:Date,
        public statusOrdemServico:string,
        public descricao:string
    ){}
}

export interface OrdemServicoFormProps {
    
    ordemServicoExistente?: OrdemServico
}