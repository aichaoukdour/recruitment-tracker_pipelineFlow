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
        Condidat condidat = entretien.getCondidat();
        String emailSubject = "Entretien Confirmation";
        
        String emailBody = buildEntretienConfirmationEmailBody(entretien, condidat);
        String smsText = buildEntretienConfirmationSmsText(entretien);
        
        // Send email notification
        try {
            emailService.sendEmail(condidat.getEmail(), emailSubject, emailBody);
            log.info("Email sent successfully to condidat: {}", condidat.getEmail());
        } catch (Exception e) {
            log.error("Failed to send email to condidat: {}", condidat.getEmail(), e);
        }
        
        // Send SMS notification if phone number is available
        if (condidat.getNumeroTelephonique() != null && !condidat.getNumeroTelephonique().isEmpty()) {
            try {
                smsService.sendSms(condidat.getNumeroTelephonique(), smsText);
                log.info("SMS sent successfully to condidat: {}", condidat.getNumeroTelephonique());
            } catch (Exception e) {
                log.error("Failed to send SMS to condidat: {}", condidat.getNumeroTelephonique(), e);
            }
        } else {
            log.info("No phone number provided for condidat: {}", condidat.getEmail());
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
                "Your interview is confirmed for %s at %s. Details sent to your email.",
                entretien.getDebut().format(DATE_FORMATTER),
                entretien.getLocation()
        );
    }
}
