package com.example.GestionDesEntretient.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestionDesEntretient.entities.Entretien;

@Repository
public interface EntretienRepository extends JpaRepository<Entretien, Long> {
    List<Entretien> findByCondidatId(Long condidatId);
    List<Entretien> findByRecruteurId(Long recruteurId);
}
