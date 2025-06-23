package com.ogooueTechnology.referentiel.repository;


import com.ogooueTechnology.referentiel.model.Utilisateur;
import com.ogooueTechnology.referentiel.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationRipository extends JpaRepository<Validation, Long> {

    Optional<Validation> findByCode(String code);
    Optional<Validation> findByUtilisateur(Utilisateur utilisateur);

}
