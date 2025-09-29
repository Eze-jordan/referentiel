package com.ogooueTechnology.referentiel.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Service
public class NotificationService {
    JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void envoyerIdentifiants(String email, String nom, String prenom, String motDePasse) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreply@solutech-one.com");
            helper.setTo(email);
            helper.setSubject("Vos identifiants de connexion");

            String htmlContent = """
        <div style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 30px;">
            <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                <h2 style="text-align: center; color: #2c3e50;">Bienvenue</h2>
                <p>Bonjour <strong>%s %s</strong>,</p>
                <p>Votre compte a été créé avec succès. Voici vos identifiants de connexion :</p>
                <div style="margin: 20px 0; padding: 15px; background: #eef; border-radius: 5px;">
                    <p><strong>Identifiant (email) :</strong> %s</p>
                    <p><strong>Mot de passe :</strong> %s</p>
                </div>
                <p>⚠️ Par mesure de sécurité, merci de changer votre mot de passe après votre première connexion.</p>
                <p style="text-align: center; color: #aaa; margin-top: 20px;">— L’équipe SoluTech-One</p>
            </div>
        </div>
        """.formatted(nom, prenom, email, motDePasse);

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}