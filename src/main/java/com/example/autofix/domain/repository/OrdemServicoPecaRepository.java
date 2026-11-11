
package com.example.autofix.domain.repository;

import com.example.autofix.domain.entities.OrdemServicoPeca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoPecaRepository
        extends JpaRepository<OrdemServicoPeca, Long> {

    List<OrdemServicoPeca> findByOrdemServico_Id(Long ordemServicoId);
}
