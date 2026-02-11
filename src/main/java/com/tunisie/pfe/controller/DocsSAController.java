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
@RequestMapping("/api/docssa")
@CrossOrigin(origins = "*")
public class DocsSAController {

    @Autowired
    private DocumentRepository documentRepository;

    // 📁 Dossier SA
    private final Path baseDir = Paths.get("documents/sa");

    // ===========================
    // ✅ 1️⃣ AJOUTER DOCUMENT SA
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
            document.setFichier("sa/" + file.getOriginalFilename());
            document.setIcone("default.png");
            document.setType("sa");

            documentRepository.save(document);

            return ResponseEntity.ok("Document SA ajouté avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur upload SA");
        }
    }

    // ===========================
    // ✅ 2️⃣ AFFICHER TOUS LES DOCS SA
    // ===========================
    @GetMapping
    public List<Document> getAllSA() {
        return documentRepository.findByType("sa");
    }

    // ===========================
    // ✅ 3️⃣ MODIFIER DOCUMENT SA
    // ===========================
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "titre", required = false) String titre) {

        try {

            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            if (!document.getType().equals("sa")) {
                return ResponseEntity.badRequest().body("Type incorrect");
            }

            if (titre != null && !titre.isEmpty()) {
                document.setTitre(titre);
            }

            if (file != null) {

                Files.createDirectories(baseDir);

                Path filePath = baseDir.resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                document.setFichier("sa/" + file.getOriginalFilename());
            }

            documentRepository.save(document);

            return ResponseEntity.ok("Document SA modifié");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur modification SA");
        }
    }

    // ===========================
    // ✅ 4️⃣ SUPPRIMER DOCUMENT SA
    // ===========================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        try {

            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            if (!document.getType().equals("sa")) {
                return ResponseEntity.badRequest().body("Type incorrect");
            }

            Path filePath = Paths.get("documents").resolve(document.getFichier());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            documentRepository.delete(document);

            return ResponseEntity.ok("Document SA supprimé");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur suppression SA");
        }
    }
}
