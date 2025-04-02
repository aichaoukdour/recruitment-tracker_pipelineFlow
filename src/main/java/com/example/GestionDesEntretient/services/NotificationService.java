package com.example.GestionDesEntretient.services;

import com.example.GestionDesEntretient.entities.Entretien;

public interface NotificationService {

    void sendEntretienConfirmation(Entretien entretien);

    void sendEntretienUpdateNotification(Entretien entretien);

    void sendEntretienCancellationNotification(Entretien entretien);
}