package com.example.GestionDesEntretient.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestionDesEntretient.dto.EntretienRequestDTO;
import com.example.GestionDesEntretient.dto.EntretienResponseDTO;
import com.example.GestionDesEntretient.services.EntretienService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/entretiens")
@RequiredArgsConstructor
public class EntretienController {

    private final EntretienService entretienService;

    @PostMapping
    public ResponseEntity<EntretienResponseDTO> createEntretien(@Valid @RequestBody EntretienRequestDTO EntretienDto) {
        EntretienResponseDTO createdEntretien = entretienService.createEntretien(EntretienDto);
        return new ResponseEntity<>(createdEntretien, HttpStatus.CREATED);
    }
    /*
     * @GetMapping("/{id}")
     * public ResponseEntity<EntretienResponseDTO> getEntretienById(@PathVariable
     * Long id) {
     * EntretienResponseDTO Entretien = entretienService.getEntretienById(id);
     * return ResponseEntity.ok(Entretien);
     * }
     */

    @GetMapping
    public ResponseEntity<List<EntretienResponseDTO>> getAllEntretiens() {
        List<EntretienResponseDTO> Entretiens = entretienService.getAllEntretiens();
        return ResponseEntity.ok(Entretiens);
    }

    /*
     * @GetMapping("/candidate/{candidateId}")
     * public ResponseEntity<List<EntretienResponseDTO>>
     * getEntretiensByCandidateId(@PathVariable Long candidateId) {
     * List<EntretienResponseDTO> Entretiens =
     * entretienService.getEntretiensByCondidatId(candidateId);
     * return ResponseEntity.ok(Entretiens);
     * }
     * 
     * @GetMapping("/recruteur/{recruteurId}")
     * public ResponseEntity<List<EntretienResponseDTO>>
     * getEntretiensByRecruteurId(@PathVariable Long recruteurId) {
     * List<EntretienResponseDTO> Entretiens =
     * entretienService.getEntretiensByRecruteurId(recruteurId);
     * return ResponseEntity.ok(Entretiens);
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntretienResponseDTO> updateEntretien(
            @PathVariable Long id,
            @Valid @RequestBody EntretienRequestDTO EntretienDto) {
        EntretienResponseDTO updatedEntretien = entretienService.updateEntretien(id, EntretienDto);
        return ResponseEntity.ok(updatedEntretien);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntretien(@PathVariable Long id) {
        entretienService.deleteEntretien(id);
        return ResponseEntity.noContent().build();
    }

}