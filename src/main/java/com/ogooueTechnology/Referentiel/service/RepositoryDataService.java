package com.ogooueTechnology.Referentiel.service;

import com.ogooueTechnology.Referentiel.model.RepositoryData;
import com.ogooueTechnology.Referentiel.repository.RepositoryDataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepositoryDataService {

    private final RepositoryDataRepository repository;

    public RepositoryDataService(RepositoryDataRepository repository) {
        this.repository = repository;
    }
    // 🔍 Récupérer par catégorie
    public ResponseEntity<List<RepositoryData>> getAllByCategory(String refCategory) {
        return ResponseEntity.ok(repository.findByRefCategory(refCategory));
    }

    // 🔍 Récupérer par ID
    public ResponseEntity<?> getById(Long refID) {
        Optional<RepositoryData> data = repository.findById(refID);
        if (data.isPresent()) {
            return ResponseEntity.ok(data.get());
        } else {
            return ResponseEntity.status(404).body("Donnée non trouvée pour l'ID " + refID);
        }
    }


    // 🔍 Recherche dans toutes les colonnes textuelles
    public ResponseEntity<List<RepositoryData>> search(String keyword) {
        List<RepositoryData> results = repository
                .findByKeyValueContainingIgnoreCaseOrValue1ContainingIgnoreCaseOrValue2ContainingIgnoreCaseOrValue3ContainingIgnoreCaseOrValue4ContainingIgnoreCase(
                        keyword, keyword, keyword, keyword, keyword);
        return ResponseEntity.ok(results);
    }

    // ✅ Créer une nouvelle entrée
    public ResponseEntity<?> create(RepositoryData repositoryData) {
        try {
            RepositoryData saved = repository.save(repositoryData);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création : " + e.getMessage());
        }
    }

    // ♻️ Mettre à jour une entrée existante
    public ResponseEntity<?> update(Long refID, RepositoryData updatedData) {
        Optional<RepositoryData> existing = repository.findById(refID);
        if (existing.isPresent()) {
            RepositoryData data = existing.get();
            data.setKeyValue(updatedData.getKeyValue());
            data.setValue1(updatedData.getValue1());
            data.setValue2(updatedData.getValue2());
            data.setValue3(updatedData.getValue3());
            data.setValue4(updatedData.getValue4());
            data.setRefCategory(updatedData.getRefCategory());
            return ResponseEntity.ok(repository.save(data));
        } else {
            return ResponseEntity.status(404).body("Aucune donnée trouvée avec l'ID : " + refID);
        }
    }

    // ❌ Supprimer par ID
    public ResponseEntity<?> delete(Long refID) {
        if (repository.existsById(refID)) {
            repository.deleteById(refID);
            return ResponseEntity.ok("Suppression réussie");
        } else {
            return ResponseEntity.status(404).body("Donnée introuvable pour suppression");
        }
    }
}
