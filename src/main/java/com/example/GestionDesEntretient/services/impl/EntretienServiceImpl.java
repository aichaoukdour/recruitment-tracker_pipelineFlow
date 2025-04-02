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

    @Override
    @Transactional
    @CacheEvict(value = "entretiens", allEntries = true)
    public EntretienResponseDTO createEntretien(EntretienRequestDTO entretienDto) {
        log.info("Creating new Entretien for condidat ID: {} and recruiter ID: {}", 
                entretienDto.getCondidatId(), entretienDto.getRecruteurId());

        Condidat condidat = findCondidatById(entretienDto.getCondidatId());
        Recruteur recruteur = findRecruteurById(entretienDto.getRecruteurId());

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
        return entretienRepository.findAll().stream()
                .map(entretienMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = "entretiens", allEntries = true)
    public EntretienResponseDTO updateEntretien(Long id, EntretienRequestDTO entretienDto) {
        log.info("Updating Entretien with ID: {}", id);

        Entretien existingEntretien = findEntretienById(id);
        Condidat condidat = findCondidatById(entretienDto.getCondidatId());
        Recruteur recruteur = findRecruteurById(entretienDto.getRecruteurId());

        checkConflictingEntretien(entretienDto.getCondidatId(), entretienDto.getRecruteurId(),
                entretienDto.getDebut(), entretienDto.getFin(), existingEntretien.getId());

        validateEntretienUpdate(entretienDto);

        updateEntretienDetails(existingEntretien, entretienDto, condidat, recruteur);

        Entretien updatedEntretien = entretienRepository.save(existingEntretien);
        notificationService.sendEntretienUpdateNotification(updatedEntretien);

        return entretienMapper.toDto(updatedEntretien);
    }

    @Override
    @Transactional
    @CacheEvict(value = "entretiens", allEntries = true)
    public void deleteEntretien(Long id) {
        log.info("Deleting Entretien with ID: {}", id);

        Entretien entretien = findEntretienById(id);
        notificationService.sendEntretienCancellationNotification(entretien);
        entretienRepository.deleteById(id);

        log.info("Entretien with ID: {} deleted successfully", id);
    }

    private void checkConflictingEntretien(Long condidatId, Long recruteurId, LocalDateTime debut, LocalDateTime fin, 
                                           Long entretienIdToExclude) {
        List<Entretien> conflictingEntretiens = entretienRepository.findConflictingEntretiens(
                condidatId, recruteurId, debut, fin, entretienIdToExclude);

        if (!conflictingEntretiens.isEmpty()) {
            String message = "Conflict with the following entretiens: " + conflictingEntretiens.stream()
                    .map(e -> "ID: " + e.getId() + ", Candidat: " + e.getCondidat().getNom() + ", Recruteur: "
                            + e.getRecruteur().getNom())
                    .collect(Collectors.joining(", "));
            log.warn(message);
            throw new EntretienConflictException(
                    "Conflit détecté pour le candidat ID " + condidatId + " et le recruteur ID " + recruteurId);
        }
    }

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

    private void updateEntretienDetails(Entretien entretien, EntretienRequestDTO entretienDto, 
                                        Condidat condidat, Recruteur recruteur) {
        entretien.setDebut(entretienDto.getDebut());
        entretien.setFin(entretienDto.getFin());
        entretien.setDescription(entretienDto.getDescription());
        entretien.setLocation(entretienDto.getLocation());
        entretien.setCondidat(condidat);
        entretien.setRecruteur(recruteur);
    }

    private Condidat findCondidatById(Long id) {
        return condidatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condidat", id));
    }

    private Recruteur findRecruteurById(Long id) {
        return recruteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recruteur", id));
    }

    private Entretien findEntretienById(Long id) {
        return entretienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entretien", id));
    }
}
