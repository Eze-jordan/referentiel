package com.ogooueTechnology.referentiel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtilisateurUpdateRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email n'est pas valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    public @NotBlank(message = "Le nom est obligatoire") String getNom() {
        return nom;
    }

    public void setNom(@NotBlank(message = "Le nom est obligatoire") String nom) {
        this.nom = nom;
    }

    public @NotBlank(message = "Le prénom est obligatoire") String getPrenom() {
        return prenom;
    }

    public void setPrenom(@NotBlank(message = "Le prénom est obligatoire") String prenom) {
        this.prenom = prenom;
    }

    public @NotBlank(message = "L'email est obligatoire") @Email(message = "L'email n'est pas valide") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "L'email est obligatoire") @Email(message = "L'email n'est pas valide") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Le mot de passe est obligatoire") String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(@NotBlank(message = "Le mot de passe est obligatoire") String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
