package com.example.autofix.controllers;

import com.example.autofix.entities.OrdemServico;
import com.example.autofix.entities.Usuario;
import com.example.autofix.repository.OrdemServicoRepository;
import com.example.autofix.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordemservicos")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Tag(name = "Ordens de Serviço", description = "Grupo de APIs responsável pelo controle das ordens de serviço")
    @GetMapping
    @Operation(
            summary = "Método de consulta de ordens de serviço",
            description = "Método responsável por consultar todas as ordens de serviço cadastradas no sistema")
    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(ordemServicoRepository.findAll());
    }

    @PostMapping
    @Operation(
            summary = "Método de criação de ordem de serviço",
            description = "Método responsável por cadastrar uma nova ordem de serviço no sistema"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrdemServico> criar(@RequestBody OrdemServico ordemServico){

        var ordemServicoBanco =  ordemServicoRepository.save(ordemServico);
        return ResponseEntity.ok(ordemServicoBanco);
    }

}
