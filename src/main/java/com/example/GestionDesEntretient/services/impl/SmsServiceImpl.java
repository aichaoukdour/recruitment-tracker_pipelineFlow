package com.example.GestionDesEntretient.services.impl;

import com.example.GestionDesEntretient.services.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private static final String ACCOUNT_SID = "AC3a6f8ad66b49deffd5a807657841d55e";
    private static final String AUTH_TOKEN = "b0aa30c848a5928c9e3e172f8553ccc4";
    private static final String FROM_PHONE_NUMBER = "+17245064245";

    @Override
    public void sendSms(String toPhoneNumber, String messageContent) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(FROM_PHONE_NUMBER),
                    messageContent
            ).create();

            System.out.println("SMS sent successfully!");
        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
        }
    }
}
