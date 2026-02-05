package com.tunisie.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tunisie.pfe.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
