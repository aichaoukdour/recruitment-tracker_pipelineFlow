package com.example.GestionDesEntretient.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestionDesEntretient.entities.Recruteur;

@Repository
public interface RecruteurRepository extends JpaRepository<Recruteur, Long> {
    Optional<Recruteur> findByEmail(String email);
    boolean existsByEmail(String email);

}
