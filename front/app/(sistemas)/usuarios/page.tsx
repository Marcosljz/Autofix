import Link from "next/link";

export default function Usuarios(){
    return(
        
        <div>
            <div>
                <h1>
                    Usuarios
                </h1>
                <Link href="/usuarios/novo"></Link>
            </div>
        </div>
        >
        <div>
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>nome</th>
                        </tr>
                    </thead>
                    <body>
                        <tr>
                            <td> marcos</td>
                        </tr>
                    </body>
                </table>
            </div>
        </div>
    )
}