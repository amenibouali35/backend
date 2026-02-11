package com.tunisie.pfe.controller;

import com.tunisie.pfe.entity.Document;
import com.tunisie.pfe.repository.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/documents")
public class DocumentRestController {

    @Autowired
    private DocumentRepository documentRepository;

    private final Path uploadDir = Paths.get("documents");

    // ✅ 1️⃣ Upload avec type
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam("titre") String titre) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide");
        }

        try {
            // Crée le dossier "documents" si il n'existe pas
            Files.createDirectories(uploadDir);

            // Chemin complet du fichier à sauvegarder
            Path targetFile = uploadDir.resolve(file.getOriginalFilename());

            // Copier le fichier uploadé dans le dossier "documents"
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            // Sauvegarder dans la base de données
            Document doc = new Document();
            doc.setTitre(titre);
            doc.setFichier(file.getOriginalFilename()); // juste le nom du fichier
            doc.setIcone("default.png");
            doc.setType(type); // si tu veux garder le type
            documentRepository.save(doc);

            return ResponseEntity.ok("Fichier ajouté avec succès");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erreur serveur");
        }

        }
    


    // ✅ 2️⃣ Lire tous les documents
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    // ✅ 3️⃣ Lire par type
    @GetMapping("/type/{type}")
    public List<Document> getByType(@PathVariable String type) {
        return documentRepository.findByType(type);
    }
}
