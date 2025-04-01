package com.example.GestionDesEntretient.services.impl;

import com.example.GestionDesEntretient.services.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;  // Add this import for @Service annotation

@Service  // Annotate the class with @Service to let Spring manage it as a bean
public class SmsServiceImpl implements SmsService {

    public static final String ACCOUNT_SID = "AC3a6f8ad66b49deffd5a807657841d55e";
    public static final String AUTH_TOKEN = "b0aa30c848a5928c9e3e172f8553ccc4";
    public static final String FROM_PHONE_NUMBER = "+17245064245";

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
