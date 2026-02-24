package com.ogooueTechnology.referentiel.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogooueTechnology.referentiel.model.RepositoryData;
import com.ogooueTechnology.referentiel.projection.RefCategoryDescriptionProjection;
import com.ogooueTechnology.referentiel.repository.RepositoryDataRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
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

    public ResponseEntity<?> createBatch(List<RepositoryData> dataList) {
        try {
            List<RepositoryData> saved = repository.saveAll(dataList);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création en lot : " + e.getMessage());
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
            data.setRefDescription(updatedData.getRefDescription());
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
    // 🔹 Récupérer toutes les données

    public ResponseEntity<List<RepositoryData>> getAll() {
        List<RepositoryData> list = repository.findAll();
        return ResponseEntity.ok(list);
    }
    public ResponseEntity<List<String>> getAllDistinctRefCategories() {
        List<String> categories = repository.findDistinctRefCategory();
        return ResponseEntity.ok(categories);
    }

    public ResponseEntity<List<RefCategoryDescriptionProjection>> getAllCategoryAndDescription() {
        return ResponseEntity.ok(repository.findAllCategoryAndDescription());
    }
    public ResponseEntity<?> uploadFile(MultipartFile file) {

        try {

            List<RepositoryData> dataList = new ArrayList<>();

            // ✅ CAS JSON
            if (file.getOriginalFilename().endsWith(".json")) {

                ObjectMapper mapper = new ObjectMapper();

                dataList = mapper.readValue(
                        file.getInputStream(),
                        new TypeReference<List<RepositoryData>>() {}
                );

            }

            // ✅ CAS EXCEL
            else if (file.getOriginalFilename().endsWith(".xlsx")) {

                InputStream is = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(is);
                Sheet sheet = workbook.getSheetAt(0);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header

                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    RepositoryData data = new RepositoryData();

                    data.setKeyValue(getCellValue(row.getCell(0)));
                    data.setValue1(getCellValue(row.getCell(1)));
                    data.setValue2(getCellValue(row.getCell(2)));
                    data.setValue3(getCellValue(row.getCell(3)));
                    data.setValue4(getCellValue(row.getCell(4)));
                    data.setRefCategory(getCellValue(row.getCell(5)));
                    data.setRefDescription(getCellValue(row.getCell(6)));

                    if (data.getKeyValue() == null || data.getKeyValue().isBlank()) {
                        continue; // skip ligne vide
                    }

                    dataList.add(data);
                }

                workbook.close();
            }

            else {
                return ResponseEntity.badRequest().body("Format non supporté. Utiliser .json ou .xlsx");
            }

            repository.saveAll(dataList);

            return ResponseEntity.ok("Import réussi : " + dataList.size() + " lignes insérées");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur import : " + e.getMessage());
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }
}
