package com.example.autofix.controllers;

import com.example.autofix.entities.Peca;
import com.example.autofix.entities.Usuario;
import com.example.autofix.repository.PecaRepository;
import com.example.autofix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    @Autowired
    private PecaRepository pecaRepository;

    @GetMapping
    public ResponseEntity<?> listarTodos() {

        return ResponseEntity.ok(pecaRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Peca> criar(@RequestBody Peca peca){

        var pecaBanco =  pecaRepository.save(peca);
        return ResponseEntity.ok(pecaBanco);
    }

}
