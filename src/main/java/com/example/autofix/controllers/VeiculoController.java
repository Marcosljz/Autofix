package com.example.autofix.controllers;

import com.example.autofix.entities.Usuario;
import com.example.autofix.entities.Veiculo;
import com.example.autofix.repository.UsuarioRepository;
import com.example.autofix.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @GetMapping
    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(veiculoRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Veiculo> criar(@RequestBody Veiculo veiculo){

        var veiculoBanco = veiculoRepository.save(veiculo);
        return ResponseEntity.ok(veiculoBanco);
    }

}
