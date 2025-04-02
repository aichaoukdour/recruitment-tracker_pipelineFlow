package com.example.GestionDesEntretient.services;

import java.util.List;

import com.example.GestionDesEntretient.dto.EntretienRequestDTO;
import com.example.GestionDesEntretient.dto.EntretienResponseDTO;

public interface EntretienService {

    EntretienResponseDTO createEntretien(EntretienRequestDTO entretienDto);

    List<EntretienResponseDTO> getAllEntretiens();

    EntretienResponseDTO updateEntretien(Long id, EntretienRequestDTO entretienDto);

    void deleteEntretien(Long id);
}
