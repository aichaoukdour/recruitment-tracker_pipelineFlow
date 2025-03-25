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
                "Your Entretien is confirmed for %s at %s. Details sent to your email.",
                entretien.getDebut().format(DATE_FORMATTER),
                entretien.getLocation()
        );
    }


    
    @Override
    public void sendEntretienUpdateNotification(Entretien entretien) {
        Condidat condidat = entretien.getCondidat();
        String emailSubject = "Entretien Update";
        String emailBody = buildEntretienUpdateEmailBody(entretien, condidat);
        String smsText = buildEntretienUpdateSmsText(entretien);
        
        // Send email notification
        emailService.sendEmail(condidat.getEmail(), emailSubject, emailBody);
        
        // Send SMS notification if phone number is available
        if (condidat.getNumeroTelephonique() != null && !condidat.getNumeroTelephonique().isEmpty()) {
            smsService.sendSms(condidat.getNumeroTelephonique(), smsText);
        }
        
        log.info("Entretien update notifications sent to Condidat: {}", condidat.getEmail());
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

    @Override
    public void sendEntretienCancellationNotification(Entretien entretien) {
        Condidat condidat = entretien.getCondidat();
        String emailSubject = "Interview Cancellation";
        
        String emailBody = buildEntretienCancellationEmailBody(entretien, condidat);
        String smsText = buildEntretienCancellationSmsText(entretien);
        
        // Send email notification
        emailService.sendEmail(condidat.getEmail(), emailSubject, emailBody);
        
        // Send SMS notification if phone number is available
        if (condidat.getNumeroTelephonique() != null && !condidat.getNumeroTelephonique().isEmpty()) {
            smsService.sendSms(condidat.getNumeroTelephonique(), smsText);
        }
        
        log.info("Interview cancellation notifications sent to condidat: {}", condidat.getEmail());
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
}


