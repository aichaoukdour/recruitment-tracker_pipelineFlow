package com.example.GestionDesEntretient.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruteurDto implements Serializable {

    private Long id;

    @JsonProperty("pre_nom")
    private String prenom;

    @JsonProperty("nom")
    private String nom;
    
    private String email;

}
