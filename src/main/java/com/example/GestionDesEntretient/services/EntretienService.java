package com.example.GestionDesEntretient.services;

import java.util.List;

import com.example.GestionDesEntretient.dto.EntretienRequestDTO;
import com.example.GestionDesEntretient.dto.EntretienResponseDTO;

import jakarta.validation.Valid;


public interface EntretienService {

    EntretienResponseDTO createEntretien(EntretienRequestDTO EntretienDto);
    List<EntretienResponseDTO> getAllEntretiens();
    EntretienResponseDTO updateEntretien(Long id, EntretienRequestDTO entretienDto);
    void deleteEntretien(Long id);
   /*  EntretienResponseDTO getEntretienById(Long id);
    List<EntretienResponseDTO> getAllEntretiens();
    List<EntretienResponseDTO> getEntretiensByCondidatId(Long condidatId);
    List<EntretienResponseDTO> getEntretiensByRecruteurId(Long recruteurId);
    EntretienResponseDTO updateEntretien(Long id, EntretienResponseDTO entretienDto);
    void deleteEntretien(Long id);*/ 
} 