package com.ogooueTechnology.referentiel.service;

import com.ogooueTechnology.referentiel.model.Validation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Service
public class NotificationService {
    JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void envoyer(Validation validation) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("notify@eservices-gabon.com");
            helper.setTo(validation.getUtilisateur().getEmail());
            helper.setSubject("Votre code d'activation");

            String htmlContent = """
            <div style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 30px;">
                <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                    <h2 style="text-align: center; color: #2c3e50;">Activation de compte</h2>
                    <p>Bonjour <strong>%s</strong>,</p>
                    <p>Merci de vous être inscrit sur MonBonDocteur.</p>
                    <p>Voici votre code d'activation :</p>
                    <div style="text-align: center; font-size: 24px; font-weight: bold; margin: 20px 0; background: #eef; padding: 15px; border-radius: 5px;">%s</div>
                    <p>⏳ Ce code est valable pendant 60 minutes.</p>
                    <p style="color: #888; font-size: 12px; text-align: center;">
                        Si vous n'avez pas demandé cette inscription, veuillez ignorer ce message.
                    </p>
                    <p style="text-align: center; color: #aaa; margin-top: 20px;">— L’équipe MonBonDocteur</p>
                </div>
            </div>
        """.formatted(validation.getUtilisateur().getNom(), validation.getCode());

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}