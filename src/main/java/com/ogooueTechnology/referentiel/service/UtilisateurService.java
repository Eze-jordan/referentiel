package com.ogooueTechnology.referentiel.service;

import com.ogooueTechnology.referentiel.dto.UtilisateurRequestDTO;
import com.ogooueTechnology.referentiel.dto.UtilisateurResponseDTO;
import com.ogooueTechnology.referentiel.mapper.UtilisateurMapper;
import com.ogooueTechnology.referentiel.model.Role;
import com.ogooueTechnology.referentiel.model.Utilisateur;
import com.ogooueTechnology.referentiel.model.Validation;
import com.ogooueTechnology.referentiel.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private  final ValidationService validationService;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, ValidationService validationService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;
    }
    //Créer un nouvel utilisateur
    public UtilisateurResponseDTO createUtilisateur(UtilisateurRequestDTO dto) {
        // Rôle par défaut
        if (dto.getRole() == null) {
            dto.setRole(Role.USER);
        }
        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getPassword())); // encodage du mot de passe
        Utilisateur saved = utilisateurRepository.save(utilisateur);
        this.validationService.enregister(utilisateur);

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
    public void activation(Map<String, String> activation) {
        Validation validation = validationService.lireEnFonctionDuCode(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new RuntimeException("Votre code a expiré");
        }

        Utilisateur utilisateurActiver = utilisateurRepository.findById(validation.getUtilisateur().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));

        utilisateurActiver.setActif(true);
        utilisateurRepository.save(utilisateurActiver);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurRepository.findByEmail(username).orElseThrow (()
                -> new UsernameNotFoundException(
                "Aucun utilisateur ne conrespond à cet identifiant"
        ));
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
