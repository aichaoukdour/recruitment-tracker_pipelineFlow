package com.example.GestionDesEntretient.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GestionDesEntretient.entities.Entretien;

@Repository
public interface EntretienRepository extends JpaRepository<Entretien, Long> {
    
    List<Entretien> findByCondidatId(Long condidatId);
    List<Entretien> findByRecruteurId(Long recruteurId);
    // Méthode pour récupérer les entretiens en conflit
@Query("SELECT e FROM Entretien e WHERE " +
"(e.condidat.id = :condidatId OR e.recruteur.id = :recruteurId) " +
"AND e.id <> :entretienId " +
"AND ((e.debut BETWEEN :newDebut AND :newFin) OR " +
"(e.fin BETWEEN :newDebut AND :newFin) OR " +
"(:newDebut BETWEEN e.debut AND e.fin) OR " +
"(:newFin BETWEEN e.debut AND e.fin))")
List<Entretien> findConflictingEntretiens(
 @Param("condidatId") Long condidatId,
 @Param("recruteurId") Long recruteurId,
 @Param("newDebut") LocalDateTime newDebut,
 @Param("newFin") LocalDateTime newFin,
 @Param("entretienId") Long entretienId);


}
