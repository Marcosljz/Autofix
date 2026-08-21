package com.example.autofix.controllers;

import com.example.autofix.entities.OrdemServico;
import com.example.autofix.entities.Usuario;
import com.example.autofix.repository.OrdemServicoRepository;
import com.example.autofix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordemservicos")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @GetMapping
    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(ordemServicoRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrdemServico> criar(@RequestBody OrdemServico ordemServico){

        var ordemServicoBanco =  ordemServicoRepository.save(ordemServico);
        return ResponseEntity.ok(ordemServicoBanco);
    }

}
