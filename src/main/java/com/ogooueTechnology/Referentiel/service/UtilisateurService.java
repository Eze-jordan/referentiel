package com.ogooueTechnology.Referentiel.service;

import com.ogooueTechnology.Referentiel.dto.UtilisateurRequestDTO;
import com.ogooueTechnology.Referentiel.dto.UtilisateurResponseDTO;
import com.ogooueTechnology.Referentiel.mapper.UtilisateurMapper;
import com.ogooueTechnology.Referentiel.model.Utilisateur;
import com.ogooueTechnology.Referentiel.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //Créer un nouvel utilisateur
    public UtilisateurResponseDTO createUtilisateur(UtilisateurRequestDTO dto) {
        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getPassword())); // encodage du mot de passe
        Utilisateur saved = utilisateurRepository.save(utilisateur); // ✅ U en minuscule
        return UtilisateurMapper.toDto(saved);
    }
//Liste tous les utilisateurs
    public List<UtilisateurResponseDTO> getAllUtilisateurs() {
        return utilisateurRepository.findAll() //
                .stream()
                .map(UtilisateurMapper::toDto)
                .collect(Collectors.toList());
    }
//Trouver un utilisateur par ID
    public UtilisateurResponseDTO getById(Long id) {
        return utilisateurRepository.findById(id) //
                .map(UtilisateurMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id " + id));
    }
//Supprimer un utilisateur
    public void deleteById(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Aucun utilisateur trouvé avec l'id : " + id);
        }
        utilisateurRepository.deleteById(id);
    }
//Modifier les infos d’un utilisateur
    public UtilisateurResponseDTO updateUtilisateur(Long id, UtilisateurRequestDTO dto) {
        Utilisateur existing = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id " + id));

        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setRole(dto.getRole());

        // Met à jour le mot de passe uniquement s’il est non vide
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existing.setMotDePasse(passwordEncoder.encode(dto.getPassword()));
        }

        Utilisateur updated = utilisateurRepository.save(existing);
        return UtilisateurMapper.toDto(updated);
    }
//Activer un compte utilisateur
    public UtilisateurResponseDTO activerUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id " + id));

        utilisateur.setActif(true);
        Utilisateur updated = utilisateurRepository.save(utilisateur);
        return UtilisateurMapper.toDto(updated);
    }



}
