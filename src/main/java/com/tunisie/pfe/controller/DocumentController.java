/*package com.tunisie.pfe.controller;

import com.tunisie.pfe.entity.Document;
import com.tunisie.pfe.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/document")
@CrossOrigin("*")
public class DocumentController {

    // ✅ Chemin unifié pour tous les fichiers
    //private final String uploadDir = "C:/spring_uploads/documents/";
    
    // ✅ Extensions autorisées
   // private final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
      //  "pdf", "doc", "docx", "xls", "xlsx", "txt"
    //);

    //@Autowired
  //  private DocumentRepository documentRepository;

    // 🔹 GET : liste des documents
//    @GetMapping
//    public ResponseEntity<List<Document>> getAllDocuments() {
//        try {
//            List<Document> documents = documentRepository.findAll();
//            return ResponseEntity.ok(documents);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    // 🔹 POST : upload fichier avec validation
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadDocument(
//            @RequestParam("titre") String titre,
//            @RequestParam("file") MultipartFile file
//    ) {
//        try {
//            // ✅ Vérification fichier vide
//            if (file.isEmpty()) {
//                return ResponseEntity.badRequest()
//                    .body("Le fichier est vide");
//            }
//
//            // ✅ Vérification extension
//            String originalFilename = file.getOriginalFilename();
//            String extension = getFileExtension(originalFilename);
//            
//            if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
//                return ResponseEntity.badRequest()
//                    .body("Type de fichier non autorisé. Extensions autorisées : " + ALLOWED_EXTENSIONS);
//            }
//
//            // ✅ Créer dossier s'il n'existe pas
//            File folder = new File(uploadDir);
//            if (!folder.exists()) {
//                folder.mkdirs();
//            }
//
//            // ✅ Nom unique avec timestamp
//            String filename = System.currentTimeMillis() + "_" + originalFilename;
//            Path filePath = Paths.get(uploadDir + filename);
//            
//            // ✅ Sauvegarder le fichier
//            Files.copy(file.getInputStream(), filePath);
//
//            // ✅ Déterminer l'icône selon l'extension
//            String icone = determineIcone(extension);
//
//            // ✅ Sauvegarder en DB
//            Document doc = new Document();
//            doc.setTitre(titre);
//            doc.setFichier(filename);
//            doc.setIcone(icone);
//            
//            Document savedDoc = documentRepository.save(doc);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedDoc);
//
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Erreur lors de l'upload : " + e.getMessage());
//        }
//    }

    // 🔹 GET : téléchargement sécurisé
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            // ✅ Récupérer le document depuis DB
            Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            String filename = document.getFichier();
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            
            // ✅ Vérifier que le fichier existe
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            // ✅ Déterminer le type MIME
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + document.getTitre() + "." + getFileExtension(filename) + "\"")
                .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 🔹 DELETE : supprimer un document
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        try {
            Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            // ✅ Supprimer le fichier physique
            Path filePath = Paths.get(uploadDir + document.getFichier());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            // ✅ Supprimer de la DB
            documentRepository.deleteById(id);

            return ResponseEntity.ok()
                .body("Document supprimé avec succès");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // 🔹 GET : visualiser un document (pour afficher dans le navigateur)
    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long id) {
        try {
            Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            Path filePath = Paths.get(uploadDir).resolve(document.getFichier()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // ✅ inline = afficher dans le navigateur au lieu de télécharger
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getFichier() + "\"")
                .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ====== Méthodes utilitaires ======

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private String determineIcone(String extension) {
        switch (extension.toLowerCase()) {
            case "pdf":
                return "pdf";
            case "doc":
            case "docx":
                return "word";
            case "xls":
            case "xlsx":
                return "excel";
            case "txt":
                return "text";
            default:
                return "file";
        }
    }
}*/