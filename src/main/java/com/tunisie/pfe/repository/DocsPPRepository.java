package com.tunisie.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tunisie.pfe.entity.DocsPP;

@Repository
public interface DocsPPRepository extends JpaRepository<DocsPP, Long> {
    // JpaRepository fournit déjà toutes les méthodes CRUD
}
