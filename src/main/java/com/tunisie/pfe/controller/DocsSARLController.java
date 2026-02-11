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
@RequestMapping("/api/docssarl")
@CrossOrigin(origins = "*")
public class DocsSARLController {

    @Autowired
    private DocumentRepository documentRepository;

    // 📁 Dossier SARL
    private final Path baseDir = Paths.get("documents/sarl");

    // ===========================
    // ✅ 1️⃣ AJOUTER DOCUMENT SARL
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
            document.setFichier("sarl/" + file.getOriginalFilename());
            document.setIcone("default.png");
            document.setType("sarl");

            documentRepository.save(document);

            return ResponseEntity.ok("Document SARL ajouté avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur upload SARL");
        }
    }

    // ===========================
    // ✅ 2️⃣ AFFICHER TOUS LES DOCS SARL
    // ===========================
    @GetMapping
    public List<Document> getAllSARL() {
        return documentRepository.findByType("sarl");
    }

    // ===========================
    // ✅ 3️⃣ MODIFIER DOCUMENT SARL
    // ===========================
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "titre", required = false) String titre) {

        try {

            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            if (!document.getType().equals("sarl")) {
                return ResponseEntity.badRequest().body("Type incorrect");
            }

            if (titre != null && !titre.isEmpty()) {
                document.setTitre(titre);
            }

            if (file != null) {

                Files.createDirectories(baseDir);

                Path filePath = baseDir.resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                document.setFichier("sarl/" + file.getOriginalFilename());
            }

            documentRepository.save(document);

            return ResponseEntity.ok("Document SARL modifié avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur modification SARL");
        }
    }

    // ===========================
    // ✅ 4️⃣ SUPPRIMER DOCUMENT SARL
    // ===========================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        try {

            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            if (!document.getType().equals("sarl")) {
                return ResponseEntity.badRequest().body("Type incorrect");
            }

            Path filePath = Paths.get("documents").resolve(document.getFichier());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            documentRepository.delete(document);

            return ResponseEntity.ok("Document SARL supprimé avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur suppression SARL");
        }
    }
}
