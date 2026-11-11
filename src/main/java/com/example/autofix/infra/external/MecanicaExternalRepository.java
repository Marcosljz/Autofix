
package com.example.autofix.infra.external;

import com.example.autofix.domain.interfaces.IMecanicaExternalRepository;
import com.example.autofix.infra.dto.CnpjResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class MecanicaExternalRepository implements IMecanicaExternalRepository {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CnpjResponse buscarMecanicaPorCnpj(String cnpj) {

        var url = "https://brasilapi.com.br/api/cnpj/v1/"
                + cnpj.replaceAll("[^0-9]", "");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                return objectMapper.readValue(
                        response.body(),
                        CnpjResponse.class
                );
            }

            throw new IllegalArgumentException("CNPJ inválido ou não encontrado!");

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar a BrasilAPI!", e);
        }
    }
}
