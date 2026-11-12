export class OrdemServico {
  constructor(
    public id: number | null,
    public dataAbertura: Date | string,
    public dataConclusao: Date | string | null,
    public statusOrdemServico: string,
    public descricao: string,
    public veiculoId: number | null,
  ) {}
}

export interface OrdemServicoFormProps {
  ordemServicoExistente?: OrdemServico;
}

export interface OrdemServicoPecaRequest {
  ordemServicoId: number;
  pecaId: number;
  quantidade: number;
}
