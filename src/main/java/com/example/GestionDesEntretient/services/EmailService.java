package com.example.GestionDesEntretient.services;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
