package com.example.autofix.infra.configuration;


import com.example.autofix.application.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

        ) {
            filterChain.doFilter(request, response);
            return;
        }


        String authHeader = request.getHeader("Authorization");


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", ""); // remove o prefixo "Bearer " e fica só com o token puro


            try{

                var usuarioLogado = tokenService.verificarToken(token);


                var user = new UsernamePasswordAuthenticationToken(
                        usuarioLogado,
                        null,
                        usuarioLogado.authorities()
                );

                SecurityContextHolder.getContext().setAuthentication(user);

            } catch (Exception e) {

                response.setStatus((HttpServletResponse.SC_UNAUTHORIZED));
                response.getWriter().println("Token inválido");
                return;
            }
        }else {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Token inválido");
            return;
        }


        filterChain.doFilter(request,response);

    }

}

