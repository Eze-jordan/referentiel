package com.ogooueTechnology.referentiel.repository;

import com.ogooueTechnology.referentiel.model.RepositoryData;
import com.ogooueTechnology.referentiel.projection.RefCategoryDescriptionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryDataRepository extends JpaRepository<RepositoryData, Long> {

    // Récupérer tous les éléments appartenant à une catégorie spécifique (ex: "001" pour pays)
    List<RepositoryData> findByRefCategory(String refCategory);

    // Rechercher dans toutes les colonnes textuelles si un mot-clé y est présent (insensible à la casse)
    List<RepositoryData> findByKeyValueContainingIgnoreCaseOrValue1ContainingIgnoreCaseOrValue2ContainingIgnoreCaseOrValue3ContainingIgnoreCaseOrValue4ContainingIgnoreCase(
            String k1, String v1, String v2, String v3, String v4
    );
    @Query("SELECT DISTINCT r.refCategory FROM RepositoryData r")
    List<String> findDistinctRefCategory();

    @Query("SELECT DISTINCT r.refCategory AS refCategory, r.refDescription AS refDescription FROM RepositoryData r")
    List<RefCategoryDescriptionProjection> findAllCategoryAndDescription();
}

