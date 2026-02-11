package com.tunisie.pfe.controller;

import com.tunisie.pfe.entity.Document;
import com.tunisie.pfe.repository.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@RestController
@RequestMapping("/api/docssuarl")
@CrossOrigin(origins = "*")
public class DocsSUARLController {

    @Autowired
    private DocumentRepository documentRepository;

    // 📁 Dossier SUARL
    private final Path baseDir = Paths.get("documents/suarl");

    // ===========================
    // ✅ 1️⃣ AJOUTER DOCUMENT SUARL
    // ===========================
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titre") String titre) {

        try {

            Files.createDirectories(baseDir);

            Path filePath = baseDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Document document = new Document();
            document.setTitre(titre);
            document.setFichier("suarl/" + file.getOriginalFilename());
            document.setIcone("default.png");
            document.setType("suarl");

            documentRepository.save(document);

            return ResponseEntity.ok("Document SUARL ajouté avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur upload SUARL");
        }
    }

    // ===========================
    // ✅ 2️⃣ AFFICHER TOUS LES DOCS SUARL
    // ===========================
    @GetMapping
    public List<Document> getAllSUARL() {
        return documentRepository.findByType("suarl");
    }

    // ===========================
    // ✅ 3️⃣ MODIFIER DOCUMENT SUARL
    // ===========================
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "titre", required = false) String titre) {

        try {

            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            if (!document.getType().equals("suarl")) {
                return ResponseEntity.badRequest().body("Type incorrect");
            }

            if (titre != null && !titre.isEmpty()) {
                document.setTitre(titre);
            }

            if (file != null) {

                Files.createDirectories(baseDir);

                Path filePath = baseDir.resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                document.setFichier("suarl/" + file.getOriginalFilename());
            }

            documentRepository.save(document);

            return ResponseEntity.ok("Document SUARL modifié avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur modification SUARL");
        }
    }

    // ===========================
    // ✅ 4️⃣ SUPPRIMER DOCUMENT SUARL
    // ===========================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        try {

            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            if (!document.getType().equals("suarl")) {
                return ResponseEntity.badRequest().body("Type incorrect");
            }

            Path filePath = Paths.get("documents").resolve(document.getFichier());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            documentRepository.delete(document);

            return ResponseEntity.ok("Document SUARL supprimé avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur suppression SUARL");
        }
    }
}
