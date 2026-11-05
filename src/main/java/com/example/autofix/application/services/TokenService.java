package com.example.autofix.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.autofix.application.DTOs.LoginRequest;
import com.example.autofix.application.DTOs.UsuarioLogado;
import com.example.autofix.domain.entities.Token;
import com.example.autofix.domain.repository.TokenRepository;
import com.example.autofix.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Service responsável por GERAR e VALIDAR o token JWT usado na autenticação.
@Service
public class TokenService {

    // @Value injeta valores do application.properties/application.yml dentro dessas variáveis.
    @Value("${spring.secret}")
    private String secret;
    @Value("${spring.expiracao}")
    private long expiracao;

    @Value("${spring.emissor}")
    private String emissor;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenRepository tokenRepository;


    public String gerarToken(LoginRequest loginRequest) {

        try {
            // Define o algoritmo de assinatura (HMAC256), usando a chave secreta.
            Algorithm algorithm = Algorithm.HMAC256(secret);

            var usuario = usuarioRepository.findAll()
                    .stream().filter( u-> u.getEmail().equals(loginRequest.email()))
                    .findFirst().orElse(null);
            String token = JWT.create()
                    .withIssuer(emissor)             // quem emitiu
                    .withSubject(loginRequest.email())            // pra quem é o token (o email do usuário)
                    .withExpiresAt(getDataExpiracao()) // quando expira
                    .sign(algorithm);                 // assina o token com a chave secreta

            tokenRepository.save(new Token(token,usuario));

            return token;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

     public UsuarioLogado verificarToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verificador = JWT.require(algorithm).withIssuer(emissor).build();

        verificador.verify(token);

        var tokenBanco = tokenRepository.findTokenByToken(token).orElse(null);

        if(tokenBanco==null){
            throw  new IllegalArgumentException("Token Invalido!");
        }

        return new UsuarioLogado(tokenBanco.getUsuario());

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