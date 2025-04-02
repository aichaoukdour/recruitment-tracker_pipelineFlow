package com.example.GestionDesEntretient.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruteurs")
@AllArgsConstructor
@NoArgsConstructor
public class Recruteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "recruteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Entretien> entretien = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String firstName) {
        this.prenom = firstName;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String lastName) {
        this.nom = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Entretien> getEntretien() {
        return entretien;
    }

    public void setEntretien(Set<Entretien> entretien) {
        this.entretien = entretien;
    }

}
