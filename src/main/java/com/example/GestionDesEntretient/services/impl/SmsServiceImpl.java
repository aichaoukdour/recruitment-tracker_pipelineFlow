package com.example.GestionDesEntretient.services.impl;

import com.example.GestionDesEntretient.services.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service; // Add this import for @Service annotation

@Service // Annotate the class with @Service to let Spring manage it as a bean
public class SmsServiceImpl implements SmsService {

    // Initialize Twilio with your credentials
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static final String FROM_PHONE_NUMBER = System.getenv("TWILIO_FROM_PHONE");

    @Override
    public void sendSms(String toPhoneNumber, String messageContent) {
        try {
            // Initialize Twilio with the credentials
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            // Create a message and send it
            Message.creator(
                    new PhoneNumber(toPhoneNumber), // Recipient's phone number
                    new PhoneNumber(FROM_PHONE_NUMBER), // Your Twilio phone number
                    messageContent // The message to send
            ).create();

            System.out.println("SMS sent successfully!");
        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
        }
    }
}
