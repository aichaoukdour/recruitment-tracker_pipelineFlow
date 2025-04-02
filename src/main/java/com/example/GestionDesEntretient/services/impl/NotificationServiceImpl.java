package com.example.GestionDesEntretient.services.impl;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.example.GestionDesEntretient.entities.Condidat;
import com.example.GestionDesEntretient.entities.Entretien;
import com.example.GestionDesEntretient.services.EmailService;
import com.example.GestionDesEntretient.services.NotificationService;
import com.example.GestionDesEntretient.services.SmsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;
    private final SmsService smsService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void sendEntretienConfirmation(Entretien entretien) {
        sendNotification(entretien, "Entretien Confirmation", this::buildEntretienConfirmationEmailBody, this::buildEntretienConfirmationSmsText);
    }

    @Override
    public void sendEntretienUpdateNotification(Entretien entretien) {
        sendNotification(entretien, "Entretien Update", this::buildEntretienUpdateEmailBody, this::buildEntretienUpdateSmsText);
    }

    @Override
    public void sendEntretienCancellationNotification(Entretien entretien) {
        sendNotification(entretien, "Interview Cancellation", this::buildEntretienCancellationEmailBody, this::buildEntretienCancellationSmsText);
    }

    private void sendNotification(Entretien entretien, String emailSubject, EmailBodyBuilder emailBodyBuilder, SmsTextBuilder smsTextBuilder) {
        Condidat condidat = entretien.getCondidat();
        String emailBody = emailBodyBuilder.build(entretien, condidat);
        String smsText = smsTextBuilder.build(entretien);

        sendEmailNotification(condidat.getEmail(), emailSubject, emailBody);
        sendSmsNotification(condidat.getNumeroTelephonique(), smsText, condidat.getEmail());
    }

    private void sendEmailNotification(String email, String subject, String body) {
        try {
            emailService.sendEmail(email, subject, body);
            log.info("Email sent successfully to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", email, e);
        }
    }

    private void sendSmsNotification(String phoneNumber, String text, String email) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            try {
                smsService.sendSms(phoneNumber, text);
                log.info("SMS sent successfully to: {}", phoneNumber);
            } catch (Exception e) {
                log.error("Failed to send SMS to: {}", phoneNumber, e);
            }
        } else {
            log.info("No phone number provided for: {}", email);
        }
    }

    private String buildEntretienConfirmationEmailBody(Entretien entretien, Condidat condidat) {
        return String.format(
                "Bonjour %s %s,\n\n" +
                "Nous avons le plaisir de vous confirmer votre entretien prévu :\n\n" +
                "🗓 Date et heure : %s - %s\n" +
                "📍 Lieu : %s\n" +
                "👤 Recruteur : %s %s\n\n" +
                "%s\n\n" +
                "Merci de bien vouloir nous confirmer votre présence en répondant à cet email.\n\n" +
                "Cordialement,\n" +
                "L’équipe RH de [Recruitement.ai.Test]\n\n" +
                "---\n" +
                "[Nom de l'entreprise] | contact@monentreprise.com | www.monentreprise.com",
                condidat.getPrenom(),
                condidat.getNom(),
                entretien.getDebut().format(DATE_FORMATTER),
                entretien.getFin().format(DATE_FORMATTER),
                entretien.getLocation(),
                entretien.getRecruteur().getPrenom(),
                entretien.getRecruteur().getNom(),
                entretien.getDescription() != null ? "ℹ️ Informations supplémentaires : " + entretien.getDescription() : ""
        );
    }

    private String buildEntretienConfirmationSmsText(Entretien entretien) {
        return String.format(
                "Your Entretien is confirmed for %s at %s. Details sent to your email.",
                entretien.getDebut().format(DATE_FORMATTER),
                entretien.getLocation()
        );
    }

    private String buildEntretienUpdateEmailBody(Entretien entretien, Condidat condidat) {
        return String.format(
                "Dear %s %s,\n\n" +
                "Your interview has been updated. New details:\n" +
                "Date and Time: %s to %s\n" +
                "Location: %s\n" +
                "Interviewer: %s %s\n\n" +
                "%s\n\n" +
                "Please confirm your availability for these new arrangements.\n\n" +
                "Best regards,\n" +
                "Recruitment Team",
                condidat.getPrenom(),
                condidat.getNom(),
                entretien.getDebut().format(DATE_FORMATTER),
                entretien.getFin().format(DATE_FORMATTER),
                entretien.getLocation(),
                entretien.getRecruteur().getPrenom(),
                entretien.getRecruteur().getNom(),
                entretien.getDescription() != null ? "ℹ️ Informations supplémentaires : " + entretien.getDescription() : ""
        );
    }

    private String buildEntretienUpdateSmsText(Entretien entretien) {
        return String.format(
                "Your interview has been updated. New time: %s at %s. Check your email for details.",
                entretien.getDebut().format(DATE_FORMATTER),
                entretien.getLocation()
        );
    }

    private String buildEntretienCancellationEmailBody(Entretien entretien, Condidat condidat) {
        return String.format(
                "Dear %s %s,\n\n" +
                "We regret to inform you that your interview scheduled for %s has been cancelled.\n\n" +
                "We will contact you shortly to arrange a new interview.\n\n" +
                "We apologize for any inconvenience caused.\n\n" +
                "Best regards,\n" +
                "Recruitment Team",
                condidat.getPrenom(),
                condidat.getNom(),
                entretien.getDebut().format(DATE_FORMATTER)
        );
    }

    private String buildEntretienCancellationSmsText(Entretien entretien) {
        return String.format(
                "Your interview scheduled for %s has been cancelled. Check your email for more information.",
                entretien.getDebut().format(DATE_FORMATTER)
        );
    }

    @FunctionalInterface
    private interface EmailBodyBuilder {
        String build(Entretien entretien, Condidat condidat);
    }

    @FunctionalInterface
    private interface SmsTextBuilder {
        String build(Entretien entretien);
    }
}
