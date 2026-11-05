package com.example.autofix.domain.repository;

import com.example.autofix.domain.entities.EnumStatusPeca;
import com.example.autofix.domain.entities.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PecaRepository extends JpaRepository<Peca,Long> {

    boolean existsPecaByNomeAndMarca (String nome, String marca);

    Optional<List<Peca>> findByStatuspecaNot(EnumStatusPeca status);


}
