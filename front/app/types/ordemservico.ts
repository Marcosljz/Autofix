
export class OrdemServico {
    constructor(
        public id: number | null,
        public dataAbertura: Date,
        public dataConclusao: Date,
        public statusOrdemServico: string,
        public descricao: string,
        public veiculoId: number | null = null
    ) {}
}

export interface OrdemServicoFormProps {
    ordemServicoExistente?: OrdemServico
}
