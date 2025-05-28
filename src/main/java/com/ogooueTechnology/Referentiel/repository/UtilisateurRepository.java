package com.ogooueTechnology.Referentiel.repository;

import com.ogooueTechnology.Referentiel.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);


}
