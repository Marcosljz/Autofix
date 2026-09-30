package com.example.autofix.configuration;


import com.example.autofix.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//Aula27/08

// Filtro que intercepta TODA requisição que chega na API antes dela chegar no Controller.
// Serve pra checar se quem está chamando a rota tem um token JWT válido (ou seja, está autenticado).
// Extends OncePerRequestFilter garante que ele rode só uma vez por requisição.
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    //iNJENÇÃO DE DEPENDENCIA
    private TokenService tokenService;


    // Método principal do filtro, chamado automaticamente pelo Spring a cada requisição.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String uri = request.getRequestURI();

        // Lista de rotas "públicas" que NÃO exigem token (login, Swagger, etc.).
        // Se a URL começar com alguma dessas, deixa passar direto (filterChain.doFilter) sem checar token.
        if (uri.startsWith("/swagger-ui")
                || uri.startsWith("/v2/api-docs")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-resources")
                || uri.startsWith("wejars")
                || uri.startsWith("/login")
                || uri.startsWith("/")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // Pega o cabeçalho "Authorization" da requisição (é onde o front manda o token).
        String authHeader = request.getHeader("Authorization");

        // O token vem no formato "Bearer <token>", então confere se começa com "Bearer ".
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", ""); // remove o prefixo "Bearer " e fica só com o token puro


            try {
                // Valida o token (assinatura, expiração, emissor) usando o TokenService.
                var jwtValidador = tokenService.verificarToken(token);

                System.out.println(jwtValidador.getSubject()); // imprime o "dono" do token (o e-mail usado no login)

            } catch (Exception e) {
                // Token inválido/expirado -> bloqueia a requisição com 401 (Unauthorized)
                response.setStatus((HttpServletResponse.SC_UNAUTHORIZED));
                response.getWriter().println("Token inválido");
                return;
            }
        }else {
            // Não veio nenhum header "Authorization: Bearer ..." -> também bloqueia com 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Token inválido");
            return;
        }

        // Token válido -> deixa a requisição seguir pro Controller normalmente.
        filterChain.doFilter(request,response);

    }

}

