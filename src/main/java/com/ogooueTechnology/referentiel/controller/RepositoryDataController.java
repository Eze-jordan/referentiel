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



}
