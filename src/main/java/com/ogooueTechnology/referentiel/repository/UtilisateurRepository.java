package com.ogooueTechnology.referentiel.repository;

import com.ogooueTechnology.referentiel.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);


}