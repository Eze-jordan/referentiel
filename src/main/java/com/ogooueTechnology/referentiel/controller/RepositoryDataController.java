package com.ogooueTechnology.referentiel.controller;

import com.ogooueTechnology.referentiel.model.RepositoryData;
import com.ogooueTechnology.referentiel.service.RepositoryDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/referentiel")
@Tag(name = "Référentiel", description = "API pour gérer les entrées du référentiel")
public class RepositoryDataController {

    private final RepositoryDataService repositoryDataService;

    public RepositoryDataController(RepositoryDataService repositoryDataService) {
        this.repositoryDataService = repositoryDataService;
    }

    @Operation(summary = "Récupérer toutes les données du référentiel")
    @GetMapping("/all")
    public ResponseEntity<List<RepositoryData>> getAll() {
        return repositoryDataService.getAll();
    }

    @Operation(summary = "Récupérer les données par catégorie")
    @GetMapping("/categorie/{refCategory}")
    public ResponseEntity<List<RepositoryData>> getByCategory(@PathVariable String refCategory) {
        return repositoryDataService.getAllByCategory(refCategory);
    }

    @Operation(summary = "Récupérer une donnée par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return repositoryDataService.getById(id);
    }

    @Operation(summary = "Rechercher une donnée par mot-clé")
    @GetMapping("/search")
    public ResponseEntity<List<RepositoryData>> search(@RequestParam("q") String keyword) {
        return repositoryDataService.search(keyword);
    }

    @Operation(summary = "Créer une nouvelle entrée dans le référentiel")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RepositoryData data) {
        return repositoryDataService.create(data);
    }

    @Operation(summary = "Créer plusieurs entrées dans le référentiel")
    @PostMapping("/batch")
    public ResponseEntity<?> createBatch(@RequestBody List<RepositoryData> dataList) {
        return repositoryDataService.createBatch(dataList);
    }

    @Operation(summary = "Modifier une entrée existante")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RepositoryData updated) {
        return repositoryDataService.update(id, updated);
    }

    @Operation(summary = "Supprimer une entrée du référentiel")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return repositoryDataService.delete(id);
    }

    @GetMapping("/categories")
    @Operation(summary = "récupère toutes les valeurs distinctes de refCategory")
    public ResponseEntity<List<String>> getAllRefCategories() {
        // ✅ appel via l'instance injectée
        return repositoryDataService.getAllDistinctRefCategories();
    }
}
