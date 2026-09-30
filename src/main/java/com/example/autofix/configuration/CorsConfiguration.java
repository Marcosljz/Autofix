package com.example.autofix.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Classe de configuração do Spring: libera o CORS (Cross-Origin Resource Sharing),
// ou seja, permite que o front-end (rodando em outro endereço/porta) consiga
// chamar essa API sem o navegador bloquear a requisição.
@Configuration
public class CorsConfiguration  implements WebMvcConfigurer {

    //Método que o Spring chama automaticamente pra registrar as regras de CORS.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);

        registry.addMapping("/**") // aplica a regra para TODAS as rotas da API (/usuarios, /veiculos, etc.)
                .allowedOrigins("http://localhost:3000") // só libera requisições vindas desse endereço (o front-end em Next.js)
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","PATCH","HEAD"); // métodos HTTP que o front pode usar
    }
}


