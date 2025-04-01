package com.example.GestionDesEntretient.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.example.GestionDesEntretient.dto.EntretienRequestDTO;
import com.example.GestionDesEntretient.dto.EntretienResponseDTO;
import com.example.GestionDesEntretient.entities.Condidat;
import com.example.GestionDesEntretient.entities.Entretien;
import com.example.GestionDesEntretient.entities.Recruteur;
import com.example.GestionDesEntretient.exception.EntretienConflictException;
import com.example.GestionDesEntretient.exception.ResourceNotFoundException;
import com.example.GestionDesEntretient.mapper.EntretienMapper;
import com.example.GestionDesEntretient.repositories.CondidatRepository;
import com.example.GestionDesEntretient.repositories.EntretienRepository;
import com.example.GestionDesEntretient.repositories.RecruteurRepository;
import com.example.GestionDesEntretient.services.EntretienService;
// import com.example.GestionDesEntretient.services.NotificationService;
import com.example.GestionDesEntretient.services.NotificationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntretienServiceImpl implements EntretienService {
    
    private final EntretienRepository entretienRepository;
    private final CondidatRepository condidatRepository;
    private final RecruteurRepository recruteurRepository;
    private final EntretienMapper entretienMapper;
    private final NotificationService notificationService;
    
    private void checkConflictingEntretien(Long condidatId, Long recruteurId, LocalDateTime debut, LocalDateTime fin, Long entretienIdToExclude) {
        List<Entretien> conflictingEntretiens = entretienRepository.findConflictingEntretiens(
                condidatId, recruteurId, debut, fin, entretienIdToExclude);

        if (!conflictingEntretiens.isEmpty()) {
            String message = "Conflict with the following entretiens: " + conflictingEntretiens.stream()
                .map(e -> "ID: " + e.getId() + ", Candidat: " + e.getCondidat().getNom() + ", Recruteur: " + e.getRecruteur().getNom())
                .collect(Collectors.joining(", "));
            log.warn(message);
            throw new EntretienConflictException("Conflit détecté pour le candidat ID " + condidatId + " et le recruteur ID " + recruteurId);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "entretiens", allEntries = true)
    public EntretienResponseDTO createEntretien(EntretienRequestDTO entretienDto) {
        log.info("Creating new Entretien for condidat ID: {} and recruiter ID: {}",
                entretienDto.getCondidatId(), entretienDto.getRecruteurId());
    
        Condidat condidat = condidatRepository.findById(entretienDto.getCondidatId())
            .orElseThrow(() -> new ResourceNotFoundException("Condidat", entretienDto.getCondidatId()));
        
        Recruteur recruteur = recruteurRepository.findById(entretienDto.getRecruteurId())
            .orElseThrow(() -> new ResourceNotFoundException("Recruteur", entretienDto.getRecruteurId()));

        // Check for conflicting schedules
        checkConflictingEntretien(entretienDto.getCondidatId(), entretienDto.getRecruteurId(),
                entretienDto.getDebut(), entretienDto.getFin(), 0L);

        Entretien entretien = entretienMapper.toEntity(entretienDto);
        entretien.setCondidat(condidat);
        entretien.setRecruteur(recruteur);
    
        Entretien savedEntretien = entretienRepository.save(entretien);
        notificationService.sendEntretienConfirmation(savedEntretien);
    
        return entretienMapper.toDto(savedEntretien);
    }

    @Override
    public List<EntretienResponseDTO> getAllEntretiens() {
        log.info("Fetching all entretiens");
        
        List<Entretien> entretiens = entretienRepository.findAll();

        return entretiens.stream()
                .map(entretienMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
@Transactional
@CacheEvict(value = "entretiens", allEntries = true)
public EntretienResponseDTO updateEntretien(Long id, EntretienRequestDTO entretienDto) {
    log.info("Updating Entretien with ID: {}", id);

    // Récupérer l'entretien existant
    Entretien existingEntretien = entretienRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Entretien", id));
    
    // Récupérer le candidat et le recruteur associés
    Condidat condidat = condidatRepository.findById(entretienDto.getCondidatId())
        .orElseThrow(() -> new ResourceNotFoundException("Condidat", entretienDto.getCondidatId()));
    
    Recruteur recruteur = recruteurRepository.findById(entretienDto.getRecruteurId())
        .orElseThrow(() -> new ResourceNotFoundException("Recruteur", entretienDto.getRecruteurId()));

    // Vérifier les conflits d'horaires
    checkConflictingEntretien(entretienDto.getCondidatId(), entretienDto.getRecruteurId(),
            entretienDto.getDebut(), entretienDto.getFin(), existingEntretien.getId());

    // Validation supplémentaire des données
    validateEntretienUpdate(entretienDto);

    // Mettre à jour les détails de l'entretien
    existingEntretien.setDebut(entretienDto.getDebut());
    existingEntretien.setFin(entretienDto.getFin());
    existingEntretien.setDescription(entretienDto.getDescription());
    existingEntretien.setLocation(entretienDto.getLocation());
    existingEntretien.setCondidat(condidat);
    existingEntretien.setRecruteur(recruteur);

    // Sauvegarder les modifications
    Entretien updatedEntretien = entretienRepository.save(existingEntretien);

    // Envoyer une notification de mise à jour
    notificationService.sendEntretienUpdateNotification(updatedEntretien);

    return entretienMapper.toDto(updatedEntretien);
}

// Nouvelle méthode pour valider les modifications
private void validateEntretienUpdate(EntretienRequestDTO entretienDto) {
    if (entretienDto.getDebut().isAfter(entretienDto.getFin())) {
        throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin.");
    }

    if (entretienDto.getDescription() == null || entretienDto.getDescription().isBlank()) {
        throw new IllegalArgumentException("La description de l'entretien ne peut pas être vide.");
    }

    if (entretienDto.getLocation() == null || entretienDto.getLocation().isBlank()) {
        throw new IllegalArgumentException("L'emplacement de l'entretien ne peut pas être vide.");
    }

    log.info("Validation passed for entretien update.");
}
    @Override
    @Transactional
    @CacheEvict(value = "entretiens", allEntries = true)  
    public void deleteEntretien(Long id) {
        log.info("Deleting interview with ID: {}", id);
    
        Entretien entretien = entretienRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Entretien", id));

        notificationService.sendEntretienCancellationNotification(entretien);
        entretienRepository.deleteById(id);
        log.info("Entretien with ID: {} deleted successfully", id);
    }
}
