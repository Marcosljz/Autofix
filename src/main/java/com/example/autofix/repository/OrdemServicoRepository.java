package com.example.autofix.repository;

import com.example.autofix.entities.EnumStatusOrdemServico;
import com.example.autofix.entities.OrdemServico;
import com.example.autofix.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico,Long> {

    Optional<List<OrdemServico>> findByStatusOrdemServicoNot(EnumStatusOrdemServico status);

}
