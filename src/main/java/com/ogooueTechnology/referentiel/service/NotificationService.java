package com.ogooueTechnology.referentiel.service;

import com.ogooueTechnology.referentiel.model.Validation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class NotificationService {
    JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void envoyer (Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("notify@eservices-gabon.com");
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("Votre code d'activation");
        String texte = String.format(
                "Bonjour M/Mme %s,\n" +
                        "Nous vous informons que votre demande d'inscription a été reçue.\n" +
                        "Pour finaliser votre inscription, veuillez utiliser le code d'activation suivant : %s.\n" +
                        "Ce code est valable pour les 10 prochaines minutes.\n" +
                        "Si vous n'avez pas demandé cette inscription, veuillez ignorer ce message.\n"+
                        "Cordialement,\n"+
                        "L'équipe d'ogooueTechnology.",
                validation.getUtilisateur().getNom(), validation.getCode());

        message.setText(texte);
        javaMailSender.send(message);

    }
}