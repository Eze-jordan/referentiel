package com.ogooueTechnology.referentiel.service;

import com.ogooueTechnology.referentiel.model.Utilisateur;
import com.ogooueTechnology.referentiel.model.Validation;
import com.ogooueTechnology.referentiel.repository.ValidationRipository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class ValidationService {
    private ValidationRipository validationRipository;
    private NotificationService notificationService;

    public ValidationService(ValidationRipository validationRipository, NotificationService notificationService) {
        this.validationRipository = validationRipository;
        this.notificationService = notificationService;
    }

    public void enregister(Utilisateur utilisateur) {
        Validation validation = new Validation();
        validation.setUtilisateur(utilisateur);
        Instant creation = Instant.now();
        validation.setCreation(creation);
        Instant expiration = creation.plus(60, MINUTES);
        validation.setExpiration(expiration);

        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setCode(code);

        this.validationRipository.save(validation);
        this.notificationService.envoyer(validation);
    }

    public void renvoyerCode(Utilisateur utilisateur) {
        Validation validation = validationRipository.findByUtilisateur(utilisateur)
                .orElseThrow(() -> new RuntimeException("Aucun code trouvé pour cet utilisateur"));

        Instant now = Instant.now();
        if (validation.getExpiration().isBefore(now)) {
            // Créer un nouveau code
            int randomInteger = new Random().nextInt(999999);
            String newCode = String.format("%06d", randomInteger);

            validation.setCode(newCode);
            validation.setCreation(now);
            validation.setExpiration(now.plus(60, MINUTES));

            validationRipository.save(validation);
            notificationService.envoyer(validation);
        } else {
            throw new RuntimeException("Le code actuel est encore valide");
        }
    }

    public void renvoyerNouveauCode(Utilisateur utilisateur) {
        Validation validation = validationRipository.findByUtilisateur(utilisateur)
                .orElseGet(() -> {
                    Validation v = new Validation();
                    v.setUtilisateur(utilisateur);
                    return v;
                });

        Instant now = Instant.now();
        int randomInteger = new Random().nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setCode(code);
        validation.setCreation(now);
        validation.setExpiration(now.plus(10, MINUTES));

        validationRipository.save(validation);
        notificationService.envoyer(validation);
    }

    public Validation lireEnFonctionDuCode(String code) {
        return this.validationRipository.findByCode(code).orElseThrow(() ->
                new RuntimeException("votre code est invalide"));
    }

    public ValidationRipository getValidationRipository() {
        return validationRipository;
    }

    public void setValidationRipository(ValidationRipository validationRipository) {
        this.validationRipository = validationRipository;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
