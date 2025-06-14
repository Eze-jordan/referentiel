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

    private final RepositoryDataService repositoryService;

    public RepositoryDataController(RepositoryDataService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Operation(summary = "Récupérer toutes les données du référentiel")
    @GetMapping
    public ResponseEntity<List<RepositoryData>> getAll() {
        return repositoryService.getAll();
    }

    @Operation(summary = "Récupérer les données par catégorie")
    @GetMapping("/categorie/{refCategory}")
    public ResponseEntity<List<RepositoryData>> getByCategory(@PathVariable String refCategory) {
        return repositoryService.getAllByCategory(refCategory);
    }

    @Operation(summary = "Récupérer une donnée par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return repositoryService.getById(id);
    }

    @Operation(summary = "Rechercher une donnée par mot-clé")
    @GetMapping("/search")
    public ResponseEntity<List<RepositoryData>> search(@RequestParam("q") String keyword) {
        return repositoryService.search(keyword);
    }

    @Operation(summary = "Créer une nouvelle entrée dans le référentiel")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RepositoryData data) {
        return repositoryService.create(data);
    }

    @Operation(summary = "Créer plusieurs entrées dans le référentiel")
    @PostMapping("/batch")
    public ResponseEntity<?> createBatch(@RequestBody List<RepositoryData> dataList) {
        return repositoryService.createBatch(dataList);
    }

    @Operation(summary = "Modifier une entrée existante")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RepositoryData updated) {
        return repositoryService.update(id, updated);
    }

    @Operation(summary = "Supprimer une entrée du référentiel")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return repositoryService.delete(id);
    }
}
