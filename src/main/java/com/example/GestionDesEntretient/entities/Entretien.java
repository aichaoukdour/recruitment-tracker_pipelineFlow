package com.example.GestionDesEntretient.entities;

import java.time.LocalDateTime;
import com.example.GestionDesEntretient.enume.StatutEntretien;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

@Entity
@Table(name = "entretiens")
@AllArgsConstructor
@NoArgsConstructor
public class Entretien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime debut;

    @Column(nullable = false)
    private LocalDateTime fin;

    @Column(nullable = false)
    private String location;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEntretien statut;

    @ManyToOne(optional = false)
    @JoinColumn(name = "condidat_id")
    private Condidat condidat;

    // Relationship with the Recruteur entity
    @ManyToOne(optional = false)
    @JoinColumn(name = "recruteur_id")
    private Recruteur recruteur;

    // Automatically set the default status before persisting
    @PrePersist
    public void prePersist() {
        if (statut == null) {
            statut = StatutEntretien.SCHEDULED;
        }
    }

    // Validation to ensure the end time is after the start time
    @AssertTrue(message = "End time must be after start time")
    public boolean isEndTimeAfterStartTime() {
        return fin == null || debut == null || fin.isAfter(debut);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatutEntretien getStatut() {
        return statut;
    }

    public void setStatut(StatutEntretien statut) {
        this.statut = statut;
    }

    public Condidat getCondidat() {
        return condidat;
    }

    public void setCondidat(Condidat condidat) {
        this.condidat = condidat;
    }

    public Recruteur getRecruteur() {
        return recruteur;
    }

    public void setRecruteur(Recruteur recruteur) {
        this.recruteur = recruteur;
    }

}
