// Classe que representa um usuário no frontend
export class Usuario{

    // Define os dados que um usuário possui
    constructor(
        public id: number | null,
        public nome:string,
        public email:string,
        public status:string,
        public cpf:string,
        public senha:string
    ){}
}


// Define as propriedades que podem ser recebidas pelo formulário de usuário
export interface UsuarioFormProps{

    // usuarioExistente é opcional e é usado quando estamos editando um usuário
    usuarioExistente?:Usuario
}