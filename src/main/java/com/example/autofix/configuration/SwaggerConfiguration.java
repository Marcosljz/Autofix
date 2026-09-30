package com.example.autofix.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Classe de configuração do Swagger (documentação interativa da API, acessível em /swagger-ui).
@Configuration
public class SwaggerConfiguration {

    // @Bean = esse método devolve um objeto que o Spring gerencia e usa pra montar a página do Swagger.
    @Bean
    public OpenAPI customOpenApi(){

        return new OpenAPI()
                // Diz ao Swagger que essa API usa autenticação do tipo "bearerAuth" (o cadeado que aparece nas rotas).
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                // Define o que é o "bearerAuth": um token HTTP do tipo Bearer, no formato JWT.
                // É isso que faz aparecer o campo "cole seu token aqui" no botão Authorize do Swagger.
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ))

                // Informações gerais que aparecem no topo da página do Swagger (nome, versão, descrição da API).
                .info(new Info()
                        .title("Autofix")
                        .version("1.0.0")
                        .description("Api para aula da 4 fase!")
                );
    }
}
