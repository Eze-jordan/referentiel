package com.ogooueTechnology.referentiel.controller;

import com.ogooueTechnology.referentiel.dto.AuthentificationDTO;
import com.ogooueTechnology.referentiel.dto.UtilisateurRequestDTO;
import com.ogooueTechnology.referentiel.dto.UtilisateurResponseDTO;
import com.ogooueTechnology.referentiel.model.Utilisateur;
import com.ogooueTechnology.referentiel.repository.UtilisateurRepository;
import com.ogooueTechnology.referentiel.securite.JwtService;
import com.ogooueTechnology.referentiel.service.UtilisateurService;
import com.ogooueTechnology.referentiel.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/utilisateurs")
@Tag(name = "Utilisateurs", description = "Opérations de gestion des utilisateurs et authentification")
public class UtilisateurController {
    @Autowired
    private final UtilisateurService utilisateurService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    private  final UtilisateurRepository utilisateurRepository;
    private  final ValidationService validationService;

    public UtilisateurController(UtilisateurService utilisateurService, UtilisateurRepository utilisateurRepository, ValidationService validationService) {
        this.utilisateurService = utilisateurService;
        this.utilisateurRepository = utilisateurRepository;
        this.validationService = validationService;
    }

    // 🔹 Créer un utilisateur
    @PostMapping("/create")
    @Operation(summary = "Créer un utilisateur", description = "Permet de créer un nouvel utilisateur")
    public ResponseEntity<UtilisateurResponseDTO> create(@RequestBody UtilisateurRequestDTO dto) {
        return ResponseEntity.ok(utilisateurService.createUtilisateur(dto));
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Renvoie uniquement si le code est expiré", description = "Permet de renvoyer un nouveau code à l'utilisateur uniquement si l'ancien a expiré")
    public ResponseEntity<?> resendOtp(@RequestBody UtilisateurRequestDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        validationService.renvoyerCode(utilisateur); // ou renvoyerNouveauCode()

        return ResponseEntity.ok("Nouveau code envoyé");
    }


    @PostMapping("/activation")
    @Operation(summary = "Activer un compte utilisateur", description = "Active un utilisateur via un token ou un code d’activation")
    public ResponseEntity<String> activation(@RequestBody Map<String, String> activation) {
        try {
            this.utilisateurService.activation(activation);
            return ResponseEntity.ok("Compte activé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/connexion")
    @Operation(summary = "Connexion", description = "Permet de se connecter et de récupérer un token JWT")
    public Map< String, String > connexion(@Valid @RequestBody AuthentificationDTO authentificationDTO) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (authentificationDTO.username(), authentificationDTO.motDePasse())
        );

        if(authenticate.isAuthenticated()) {
            return this.jwtService.generate(authentificationDTO.username());

        }
        return null;

    }

    // 🔹 Liste de tous les utilisateurs
    @GetMapping("/all")
    @Operation(summary = "Récupérer tous les utilisateurs", description = "Retourne la liste de tous les utilisateurs")
    public ResponseEntity<List<UtilisateurResponseDTO>> getAll() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }

    // 🔹 Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par ID", description = "Retourne les infos d’un utilisateur spécifique")
    public ResponseEntity<UtilisateurResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getById(id));
    }

    // 🔹 Mettre à jour un utilisateur
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un utilisateur", description = "Permet de modifier les infos d’un utilisateur existant")
    public ResponseEntity<UtilisateurResponseDTO> update(@PathVariable Long id, @RequestBody UtilisateurRequestDTO dto) {
        return ResponseEntity.ok(utilisateurService.updateUtilisateur(id, dto));
    }

    // 🔹 Supprimer un utilisateur
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Permet de supprimer un utilisateur par ID")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        utilisateurService.deleteById(id);
        return ResponseEntity.ok().body("Utilisateur supprimé avec succès.");
    }

    // 🔹 Activer un utilisateur
    @PutMapping("/{id}/activer")
    @Operation(summary = "activer un utilisateur par ID", description = "activer un utilisateur par ID")
    public ResponseEntity<UtilisateurResponseDTO> activer(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.activerUtilisateur(id));
    }
    @PutMapping("/{id}/desactiver")
    @Operation(summary = "Désactiver un utilisateur par ID", description = "Désactive le compte d'un utilisateur sans le supprimer.")
    public ResponseEntity<UtilisateurResponseDTO> desactiver(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.desactiverUtilisateur(id));
    }

}
