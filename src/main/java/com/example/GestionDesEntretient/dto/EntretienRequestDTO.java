package com.example.GestionDesEntretient.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntretienRequestDTO implements Serializable {

    @NotNull(message = "Start time is required")
    @Future(message = "Interview must be scheduled in the future")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("debut_de_entretien")
    private LocalDateTime debut;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("fin_de_entretien")
    private LocalDateTime fin;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Location is required")
    @Size(min = 3, max = 100, message = "Location must be between 3 and 100 characters")
    private String location;

    @NotNull(message = "Candidate ID is required")
    @JsonProperty("condidat_id")
    private Long condidatId;
    
    @NotNull(message = "Recruiter ID is required")
    @JsonProperty("recruteur_id")
    private Long recruteurId;

}

