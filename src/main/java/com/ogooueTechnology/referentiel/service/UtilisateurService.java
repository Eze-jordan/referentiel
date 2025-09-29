package com.ogooueTechnology.referentiel.service;

import com.ogooueTechnology.referentiel.dto.UtilisateurRequestDTO;
import com.ogooueTechnology.referentiel.dto.UtilisateurResponseDTO;
import com.ogooueTechnology.referentiel.mapper.UtilisateurMapper;
import com.ogooueTechnology.referentiel.model.Role;
import com.ogooueTechnology.referentiel.model.Utilisateur;
import com.ogooueTechnology.referentiel.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final NotificationService notificationService;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, NotificationService notificationService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public UtilisateurResponseDTO createUtilisateurSansOtp(UtilisateurRequestDTO dto) {
        if (dto.getRole() == null) {
            dto.setRole(Role.USER);
        }

        // Générer un mot de passe aléatoire
        String generatedPassword = generateRandomPassword(10);

        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);
        utilisateur.setMotDePasse(passwordEncoder.encode(generatedPassword));
        utilisateur.setActif(true); // pas d’OTP → on active direct

        Utilisateur saved = utilisateurRepository.save(utilisateur);

        // Envoi des identifiants par email
        notificationService.envoyerIdentifiants(
                saved.getEmail(),
                saved.getNom(),
                saved.getPrenom(),
                generatedPassword
        );

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

    // Met à jour uniquement les informations de l'utilisateur (sans le mot de passe)
    public UtilisateurResponseDTO updateInfos(Long id, UtilisateurRequestDTO dto) {
        Utilisateur existing = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id " + id));

        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setRole(dto.getRole());

        Utilisateur updated = utilisateurRepository.save(existing);
        return UtilisateurMapper.toDto(updated);
    }

    // Met à jour uniquement le mot de passe
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id " + id));

        // Vérifie l'ancien mot de passe
        if (!passwordEncoder.matches(oldPassword, utilisateur.getMotDePasse())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        // Vérifie que le nouveau mot de passe n’est pas vide
        if (newPassword == null || newPassword.isEmpty()) {
            throw new RuntimeException("Le nouveau mot de passe ne peut pas être vide");
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(utilisateur);
    }


    //Activer un compte utilisateur
    public UtilisateurResponseDTO activerUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id " + id));

        utilisateur.setActif(true);
        Utilisateur updated = utilisateurRepository.save(utilisateur);
        return UtilisateurMapper.toDto(updated);
    }

    public UtilisateurResponseDTO desactiverUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        utilisateur.setActif(false);
        utilisateurRepository.save(utilisateur);

        return UtilisateurMapper.toDto(utilisateur);
    }


}
