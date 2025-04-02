package com.example.GestionDesEntretient.services;

/**
 * Service interface for sending SMS messages.
 */
public interface SmsService {
    /**
     * Sends an SMS to the specified phone number with the given message.
     *
     * @param numeroTelephonique the recipient's phone number
     * @param message the message to be sent
     */
    void sendSms(String numeroTelephonique, String message);
}
