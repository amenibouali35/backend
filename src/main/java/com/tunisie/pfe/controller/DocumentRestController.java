package com.tunisie.pfe.controller;

import com.tunisie.pfe.dto.DocumentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentRestController {

    private final Path baseDir = Paths.get("documents");

    // 1️⃣ Lister les documents
    @GetMapping
    public ResponseEntity<List<DocumentDto>> listDocuments(
            @RequestParam(value = "path", required = false) String path) {

        Path currentPath = baseDir;
        if (path != null && !path.isEmpty()) {
            currentPath = baseDir.resolve(path);
        }

        if (!currentPath.normalize().startsWith(baseDir)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        File folder = currentPath.toFile();
        if (!folder.exists() || !folder.isDirectory()) {
            return ResponseEntity.notFound().build();
        }

        List<DocumentDto> documents = new ArrayList<>();

        for (File file : folder.listFiles()) {
            String relativePath =
                    (path == null || path.isEmpty())
                            ? file.getName()
                            : path + "/" + file.getName();

            documents.add(new DocumentDto(
                    file.getName(),
                    relativePath,
                    file.isDirectory()
            ));
        }

        return ResponseEntity.ok(documents);
    }

    // 2️⃣ Uploader un document
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "path", required = false) String path) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide");
        }

        try {
            Path targetDir = baseDir;
            if (path != null && !path.isEmpty()) {
                targetDir = baseDir.resolve(path);
            }

            if (!targetDir.normalize().startsWith(baseDir)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Files.createDirectories(targetDir);

            Path targetFile = targetDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("Fichier uploadé avec succès");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload");
        }
    }
}
