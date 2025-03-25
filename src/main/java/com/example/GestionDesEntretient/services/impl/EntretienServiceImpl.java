package com.example.GestionDesEntretient.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.example.GestionDesEntretient.dto.EntretienRequestDTO;
import com.example.GestionDesEntretient.dto.EntretienResponseDTO;
import com.example.GestionDesEntretient.entities.Condidat;
import com.example.GestionDesEntretient.entities.Entretien;
import com.example.GestionDesEntretient.entities.Recruteur;
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
    
    @Override
    @Transactional
    @CacheEvict(value = "entretiens", allEntries = true)  // Clears cache on new entry
    public EntretienResponseDTO createEntretien(EntretienRequestDTO entretienDto) {
        log.info("Creating new Entretien for condidat ID: {} and recruiter ID: {}", 
                entretienDto.getCondidatId(), entretienDto.getRecruteurId());
        
        Condidat condidat = condidatRepository.findById(entretienDto.getCondidatId())
                .orElseThrow(() -> new ResourceNotFoundException("Condidat not found with ID: " + entretienDto.getCondidatId()));
        
        Recruteur recruteur = recruteurRepository.findById(entretienDto.getRecruteurId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with ID: " + entretienDto.getRecruteurId()));
        
        Entretien entretien = entretienMapper.toEntity(entretienDto);
        entretien.setCondidat(condidat);
        entretien.setRecruteur(recruteur);
        
        Entretien savedEntretien = entretienRepository.save(entretien);
        
        // Uncomment if notification service is implemented
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
    
    Entretien existingEntretien = entretienRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Entretien not found with ID: " + id));
    
    Condidat condidat = condidatRepository.findById(entretienDto.getCondidatId())
            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + entretienDto.getCondidatId()));
    
    Recruteur recruteur = recruteurRepository.findById(entretienDto.getRecruteurId())
            .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with ID: " + entretienDto.getRecruteurId()));
    
    // Mise à jour des propriétés de l'entretien
    existingEntretien.setDebut(entretienDto.getDebut());
    existingEntretien.setFin(entretienDto.getFin());
    existingEntretien.setDescription(entretienDto.getDescription());
    existingEntretien.setLocation(entretienDto.getLocation());
    existingEntretien.setCondidat(condidat);
    existingEntretien.setRecruteur(recruteur);
    
    Entretien updatedEntretien = entretienRepository.save(existingEntretien);
    
    // Envoyer une notification sur la mise à jour
    notificationService.sendEntretienUpdateNotification(updatedEntretien);
    
    return entretienMapper.toDto(updatedEntretien);
}



@Override
@Transactional
@CacheEvict(value = "entretiens", allEntries = true)  // Clear cache on delete
public void deleteEntretien(Long id) {
    log.info("Deleting interview with ID: {}", id);
    
    // Récupérer l'entretien avant la suppression
    Entretien entretien = entretienRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Interview not found with ID: " + id));

    // Envoyer la notification avant de supprimer l'entretien
    notificationService.sendEntretienCancellationNotification(entretien);
    
    // Supprimer l'entretien
    entretienRepository.deleteById(id);
    log.info("Entretien with ID: {} deleted successfully", id);
}

}


        
