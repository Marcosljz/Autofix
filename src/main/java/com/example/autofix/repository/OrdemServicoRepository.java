package com.example.autofix.repository;

import com.example.autofix.entities.OrdemServico;
import com.example.autofix.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico,Long> {



}
