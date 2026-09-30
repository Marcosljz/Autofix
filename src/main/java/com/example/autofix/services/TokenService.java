package com.example.autofix.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Service responsável por GERAR e VALIDAR o token JWT usado na autenticação.
@Service
public class TokenService {

    // @Value injeta valores do application.properties/application.yml dentro dessas variáveis.
    @Value("spring.secret")
    private String secret; // chave secreta usada pra assinar/validar o token (só o backend conhece)

    @Value("${spring.expiracao}")
    private long expiracao; // tempo (em minutos) até o token expirar

    @Value("spring.emissor")
    private String emissor; // "quem emitiu" o token (identifica a própria aplicação)


    // Gera um token JWT novo pro "subject" informado (aqui, o email do usuário que fez login).
    public String gerarToken(String subject) {

        try {
            // Define o algoritmo de assinatura (HMAC256), usando a chave secreta.
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer(emissor)             // quem emitiu
                    .withSubject(subject)            // pra quem é o token (o email do usuário)
                    .withExpiresAt(getDataExpiracao()) // quando expira
                    .sign(algorithm);                 // assina o token com a chave secreta

            return token;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    // Valida um token recebido (usado pelo JwtFilter em toda requisição autenticada).
    // Confere a assinatura, o emissor e se não expirou. Se algo estiver errado, lança JWTVerificationException.
    public DecodedJWT verificarToken(String token) throws JWTVerificationException {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verificador = JWT.require(algorithm).withIssuer(emissor).build();

        return verificador.verify(token);

    }

    // Calcula a data/hora de expiração do token: agora + X minutos (definido em "expiracao").
    private Instant getDataExpiracao(){
        //pegar data atual

        var dataAtual = LocalDateTime.now();
        //adicionar ou diminuir tempo da data atual
        var dataFutura = dataAtual.plusMinutes(expiracao);


        //converte em Instante
        return dataFutura.toInstant(ZoneOffset.of("-03:00")); // fuso horário de Brasília
    }

}