package com.example.autofix.domain.repository;

import com.example.autofix.domain.entities.Mecanica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MecanicaRepository extends JpaRepository<Mecanica,Long> {
}
