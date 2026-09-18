package com.example.autofix.repository;

import com.example.autofix.entities.EnumStatusVeiculo;
import com.example.autofix.entities.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo,Long> {

    boolean existsVeiculoByPlaca(String placa);

    Optional<Veiculo> findByPlaca(String placa);

    Optional<List<Veiculo>> findByStatusVeiculoNot(EnumStatusVeiculo statusVeiculo);

}
