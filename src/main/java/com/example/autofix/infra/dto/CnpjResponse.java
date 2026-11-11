package com.example.autofix.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CnpjResponse {

    @JsonProperty("razao_social")
    private String razaoSocial;

    private String cnpj;

    @JsonProperty("nome_fantasia")
    private String nomeFantasia;
}