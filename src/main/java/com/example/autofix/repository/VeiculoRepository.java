package com.example.autofix.repository;

import com.example.autofix.entities.Usuario;
import com.example.autofix.entities.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo,Long> {



}
