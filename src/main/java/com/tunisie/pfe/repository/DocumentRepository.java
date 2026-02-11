package com.tunisie.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.tunisie.pfe.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    // 🔵 Chercher par type
    List<Document> findByType(String type);
}
