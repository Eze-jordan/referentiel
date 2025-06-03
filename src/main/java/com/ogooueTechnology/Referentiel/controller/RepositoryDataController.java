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


    // 🔹 Récupérer toutes les données
    @GetMapping
    public ResponseEntity<List<RepositoryData>> getAll() {
        return repositoryService.getAll();
    }


}
