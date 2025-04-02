package com.example.GestionDesEntretient.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestionDesEntretient.entities.Condidat;

@Repository
public interface CondidatRepository extends JpaRepository<Condidat, Long> {
    Optional<Condidat> findByEmail(String email);

    boolean existsByEmail(String email);
}