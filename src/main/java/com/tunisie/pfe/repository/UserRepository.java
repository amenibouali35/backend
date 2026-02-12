package com.tunisie.pfe.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tunisie.pfe.entity.user;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<user, Long> {

    // Chercher un utilisateur par email
    Optional<user> findByEmail(String email);

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);
    List<user> findByActive(Integer active);

}
