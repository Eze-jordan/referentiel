package com.ogooueTechnology.Referentiel.controller;

import com.ogooueTechnology.Referentiel.model.RepositoryData;
import com.ogooueTechnology.Referentiel.service.RepositoryDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/referentiel")
public class RepositoryDataController {

    private final RepositoryDataService repositoryService;

    public RepositoryDataController(RepositoryDataService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // 🔹 Récupérer toutes les données par catégorie
    @GetMapping("/categorie/{refCategory}")
    public ResponseEntity<List<RepositoryData>> getByCategory(@PathVariable String refCategory) {
        return repositoryService.getAllByCategory(refCategory);
    }

    // 🔹 Récupérer une donnée par son ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return repositoryService.getById(id);
    }

    // 🔹 Rechercher une valeur
    @GetMapping("/search")
    public ResponseEntity<List<RepositoryData>> search(@RequestParam("q") String keyword) {
        return repositoryService.search(keyword);
    }

    // ✅ Ajouter une nouvelle entrée
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RepositoryData data) {
        return repositoryService.create(data);
    }

    // ♻️ Modifier une entrée existante
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RepositoryData updated) {
        return repositoryService.update(id, updated);
    }

    // ❌ Supprimer une entrée
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return repositoryService.delete(id);
    }
}
