package com.ogooueTechnology.referentiel.controller;

import com.ogooueTechnology.referentiel.dto.AuthentificationDTO;
import com.ogooueTechnology.referentiel.dto.UtilisateurRequestDTO;
import com.ogooueTechnology.referentiel.dto.UtilisateurResponseDTO;
import com.ogooueTechnology.referentiel.securite.JwtService;
import com.ogooueTechnology.referentiel.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
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
public class UtilisateurController {
    @Autowired
    private final UtilisateurService utilisateurService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // 🔹 Créer un utilisateur
    @PostMapping("/create")
    public ResponseEntity<UtilisateurResponseDTO> create(@RequestBody UtilisateurRequestDTO dto) {
        return ResponseEntity.ok(utilisateurService.createUtilisateur(dto));
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
    public ResponseEntity<List<UtilisateurResponseDTO>> getAll() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }

    // 🔹 Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getById(id));
    }

    // 🔹 Mettre à jour un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> update(@PathVariable Long id, @RequestBody UtilisateurRequestDTO dto) {
        return ResponseEntity.ok(utilisateurService.updateUtilisateur(id, dto));
    }

    // 🔹 Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        utilisateurService.deleteById(id);
        return ResponseEntity.ok().body("Utilisateur supprimé avec succès.");
    }

    // 🔹 Activer un utilisateur
    @PutMapping("/{id}/activer")
    public ResponseEntity<UtilisateurResponseDTO> activer(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.activerUtilisateur(id));
    }
}
