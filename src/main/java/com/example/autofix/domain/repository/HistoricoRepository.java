
package com.example.autofix.domain.repository;

import com.example.autofix.domain.entities.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {

    List<Historico> findByUsuario_IdOrderByDataHoraDesc(Long usuarioId);

    List<Historico> findByMecanica_IdOrderByDataHoraDesc(Long mecanicaId);
}
