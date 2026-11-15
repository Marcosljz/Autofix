
package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.HistoricoResponse;
import com.example.autofix.application.services.HistoricoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historico")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    public ResponseEntity<List<HistoricoResponse>> listar() {

        return ResponseEntity.ok(
                historicoService.listarHistorico()
        );
    }
}
