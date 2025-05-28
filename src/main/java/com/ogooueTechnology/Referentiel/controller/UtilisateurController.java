package com.ogooueTechnology.Referentiel.controller;

import com.ogooueTechnology.Referentiel.dto.UtilisateurRequestDTO;
import com.ogooueTechnology.Referentiel.dto.UtilisateurResponseDTO;
import com.ogooueTechnology.Referentiel.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // 🔹 Créer un utilisateur
    @PostMapping("/create")
    public ResponseEntity<UtilisateurResponseDTO> create(@RequestBody UtilisateurRequestDTO dto) {
        return ResponseEntity.ok(utilisateurService.createUtilisateur(dto));
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
