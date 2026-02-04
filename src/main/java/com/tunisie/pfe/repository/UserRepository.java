package com.tunisie.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tunisie.pfe.entity.user;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<user, Long> {
    
    // Méthode pour chercher par email
    Optional<user> findByEmail(String email);
    
    // Méthode pour chercher par CIN
    Optional<user> findByCin(String cin);
    
    // Vérifier si un email existe
    boolean existsByEmail(String email);
    
    // Vérifier si un CIN existe
    boolean existsByCin(String cin);
}