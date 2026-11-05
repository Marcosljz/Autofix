package com.example.autofix.domain.repository;

import com.example.autofix.domain.entities.EnumStatusOrdemServico;
import com.example.autofix.domain.entities.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico,Long> {

    Optional<List<OrdemServico>> findByStatusOrdemServicoNot(EnumStatusOrdemServico status);

}
