export class Peca{
    constructor(
        public id: number | null,
        public nome:string,
        public preco:string,
        public marca:string,
        public descricao:string,
        public quantidade:string,
        public statuspeca:string
    ){}
}