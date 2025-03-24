package com.example.GestionDesEntretient.services;

import com.example.GestionDesEntretient.entities.Entretien;

public interface NotificationService {
    void sendEntretienConfirmation(Entretien entretien);
}