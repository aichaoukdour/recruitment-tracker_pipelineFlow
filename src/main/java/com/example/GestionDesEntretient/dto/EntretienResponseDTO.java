package com.example.GestionDesEntretient.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.GestionDesEntretient.enume.StatutEntretien;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntretienResponseDTO implements Serializable {

    private Long id;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("debut_de_entretien")
    private LocalDateTime debut;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("fin_de_entretien")
    private LocalDateTime fin;

    private String description;
    private String location;
    private StatutEntretien statut;
    private CondidatDto condidat;
    private RecruteurDto recruteur;

}

