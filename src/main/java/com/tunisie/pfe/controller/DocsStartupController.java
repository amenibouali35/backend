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
@RequestMapping("/api/docsstartup")
@CrossOrigin(origins = "*")
public class DocsStartupController {

    @Autowired
    private DocumentRepository documentRepository;

    // 📁 Dossier Startup
    private final Path baseDir = Paths.get("documents/startup");

    // ===========================
    // ✅ 1️⃣ AJOUTER DOCUMENT STARTUP
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
            document.setFichier("startup/" + file.getOriginalFilename());
            document.setIcone("default.png");
            document.setType("startup");

            documentRepository.save(document);

            return ResponseEntity.ok("Document STARTUP ajouté avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur upload STARTUP");
        }
    }

    // ===========================
    // ✅ 2️⃣ AFFICHER TOUS LES DOCS STARTUP
    // ===========================
    @GetMapping
    public List<Document> getAllStartup() {
        return documentRepository.findByType("startup");
    }

    // ===========================
    // ✅ 3️⃣ MODIFIER DOCUMENT STARTUP
    // ===========================
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "titre", required = false) String titre) {

        try {

            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            if (!document.getType().equals("startup")) {
                return ResponseEntity.badRequest().body("Type incorrect");
            }

            if (titre != null && !titre.isEmpty()) {
                document.setTitre(titre);
            }

            if (file != null) {

                Files.createDirectories(baseDir);

                Path filePath = baseDir.resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                document.setFichier("startup/" + file.getOriginalFilename());
            }

            documentRepository.save(document);

            return ResponseEntity.ok("Document STARTUP modifié avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur modification STARTUP");
        }
    }

    // ===========================
    // ✅ 4️⃣ SUPPRIMER DOCUMENT STARTUP
    // ===========================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        try {

            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            if (!document.getType().equals("startup")) {
                return ResponseEntity.badRequest().body("Type incorrect");
            }

            Path filePath = Paths.get("documents").resolve(document.getFichier());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            documentRepository.delete(document);

            return ResponseEntity.ok("Document STARTUP supprimé avec succès");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur suppression STARTUP");
        }
    }
}
