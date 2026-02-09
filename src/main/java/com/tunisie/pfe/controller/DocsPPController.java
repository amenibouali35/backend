package com.tunisie.pfe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.StandardCopyOption;

import com.tunisie.pfe.entity.DocsPP;
import com.tunisie.pfe.repository.DocsPPRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/docspp")
@CrossOrigin(origins = "*")
public class DocsPPController {

    // Chemin de ton dossier
    private final String uploadDir = "C:/Users/Ameni/OneDrive/Desktop/rkspacestage/pfebackend/docspp/";

    @Autowired
    private DocsPPRepository docsPPRepo;

    // ---- AJOUTER UN DOCS ----
@PostMapping("/upload")
public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {

    try {
        // Créer le dossier s'il n'existe pas
        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Créer le chemin complet pour le fichier
        Path filePath = dirPath.resolve(file.getOriginalFilename());

        // Copier le fichier en remplaçant si existant
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Enregistrer dans la base
        DocsPP doc = new DocsPP();
        doc.setNomFichier(file.getOriginalFilename());
        doc.setCheminFichier(filePath.toString());
        docsPPRepo.save(doc);

        return ResponseEntity.ok("DocsPP ajouté avec succès");

    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Erreur lors de l'upload du fichier");
    }
}
    // ---- VOIR TOUS LES DOCS ----
    @GetMapping
    public List<DocsPP> getAllDocs() {
        return docsPPRepo.findAll();
    }

    // ---- MODIFIER UN DOCS ----
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDocs(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "nomFichier", required = false) String nomFichier) {

        try {
            DocsPP doc = docsPPRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("DocsPP non trouvé"));

            // Renommer
            if (nomFichier != null && !nomFichier.isEmpty()) {
                doc.setNomFichier(nomFichier);
            }

            // Remplacer le fichier
            if (file != null) {
                Path dirPath = Paths.get(uploadDir);
                if (!Files.exists(dirPath)) Files.createDirectories(dirPath);

                Path filePath = dirPath.resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(), filePath);

                doc.setCheminFichier(filePath.toString());
                doc.setNomFichier(file.getOriginalFilename());
            }

            docsPPRepo.save(doc);
            return ResponseEntity.ok("DocsPP modifié");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la modification du fichier");
        }
    }

    // ---- SUPPRIMER UN DOCS ----
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDocs(@PathVariable Long id) {

        try {
            DocsPP doc = docsPPRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("DocsPP non trouvé"));

            // Supprimer le fichier physique
            Path filePath = Paths.get(doc.getCheminFichier());
            if (Files.exists(filePath)) Files.delete(filePath);

            // Supprimer de la base
            docsPPRepo.delete(doc);

            return ResponseEntity.ok("DocsPP supprimé");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la suppression du fichier");
        }
    }
}
