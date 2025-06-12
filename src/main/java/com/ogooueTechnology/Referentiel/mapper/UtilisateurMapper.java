package com.ogooueTechnology.referentiel.mapper;

import com.ogooueTechnology.referentiel.dto.UtilisateurRequestDTO;
import com.ogooueTechnology.referentiel.dto.UtilisateurResponseDTO;
import com.ogooueTechnology.referentiel.model.Utilisateur;

public class UtilisateurMapper {

    public static Utilisateur toEntity(UtilisateurRequestDTO dto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(dto.getPassword());
        utilisateur.setRole(dto.getRole());
        utilisateur.setActif(false); // par défaut à la création
        return utilisateur;
    }

    public static UtilisateurResponseDTO toDto(Utilisateur utilisateur) {
        UtilisateurResponseDTO dto = new UtilisateurResponseDTO();
        dto.setId(utilisateur.getId());
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setEmail(utilisateur.getEmail());
        dto.setActif(utilisateur.isActif());
        dto.setRole(utilisateur.getRole());
        return dto;
    }
}
