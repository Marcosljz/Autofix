package com.example.autofix.presentation;

import com.example.autofix.application.DTOs.AtualizarStatusVeiculoRequest;
import com.example.autofix.application.DTOs.VeiculoRequest;
import com.example.autofix.application.DTOs.VeiculoResponse;
import com.example.autofix.application.services.VeiculoService;
import com.example.autofix.domain.entities.EnumStatusVeiculo;
import com.example.autofix.domain.entities.Veiculo;
import com.example.autofix.domain.repository.VeiculoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@Tag(
        name = "Veículos",
        description = "Grupo de APIs responsável pelo controle e gerenciamento de veículos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    @Operation(summary = "Método de consulta de veículos",
            description = "Método responsável por consultar todos os veículos cadastrados no sistema")
    public ResponseEntity<List<VeiculoResponse>> listarTodos() {

        return ResponseEntity.ok(veiculoService.ListarTodosVeiculosTable());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Método de consulta de veículos por ID",
            description = "Método responsável por buscar um veículos específico através do seu ID.")
    public ResponseEntity<VeiculoResponse> buscarPorId(@PathVariable Long id){

        VeiculoResponse veiculoBanco = veiculoService.buscarPorId(id);
        if(veiculoBanco!= null ){
            return  ResponseEntity.ok(veiculoBanco);
        }

        return  ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
            summary = "Método de criação de veículo",
            description = "Método responsável por cadastrar um novo veículo no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Veiculo> criar(@RequestBody VeiculoRequest veiculo){

       try {
           VeiculoResponse veiculoBanco = veiculoService.salvarVeiculo(veiculo,0L)
       }catch (Exception e)
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Método de atualização de status de veículo",
            description = "Método responsável por atualizar apenas o status de um veículo cadastrado no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusVeiculoRequest statusRequest){

        var atualizarVeiculo = veiculoService.atualizarStatusVeiculo(statusRequest.statusVeiculo(),id);

        if (atualizarVeiculo) {
            return ResponseEntity.ok().build();
        }
        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Método de atualização de veículo",
            description = "Método responsável por atualizar todos os dados de um veículo cadastrado no sistema")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long id, @RequestBody Veiculo veiculo){

        try{
            Veiculo veiculoBanco = veiculoRepository.findById(id).orElse(null);
            if(veiculoBanco!= null ){
                veiculoBanco.setPlaca(veiculo.getPlaca());
                veiculoBanco.setModelo(veiculo.getModelo());
                veiculoBanco.setAno(veiculo.getAno());
                veiculoBanco.setCor(veiculo.getCor());
                veiculoBanco.setQuilometragem(veiculo.getQuilometragem());
                veiculoBanco.setStatusVeiculo(veiculo.getStatusVeiculo());
                veiculoRepository.save(veiculoBanco);
                return  ResponseEntity.ok().build();
            }
            return  ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping("/{id}/excluir")
    @Operation(
            summary = "Método de exclusão de veículo",
            description = "Método responsável por excluir um veículo cadastrado no sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

       var atualizarVeiculo = veiculoService.atualizarStatusVeiculo(EnumStatusVeiculo.EXCLUIDO,id);

       if (atualizarVeiculo){
           return ResponseEntity.ok().build();
        }

        return  ResponseEntity.notFound().build();
    }

}
