package com.tunisie.pfe.controller;

import com.tunisie.pfe.entity.TypeEntreprise;
import com.tunisie.pfe.repository.TypeEntrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping("/api/types-entreprise")
@CrossOrigin("*")
public class TypeEntrepriseController {

    @Autowired
    private TypeEntrepriseRepository repository;

    // 🔹 GET ALL
    @GetMapping
    public List<TypeEntreprise> getAll() {
        return repository.findAll();
    }

    // 🔹 POST
    @PostMapping
    public TypeEntreprise add(@RequestBody TypeEntreprise type) {
        return repository.save(type);
    }

    // 🔹 PUT
    @PutMapping("/{id}")
    public TypeEntreprise update(@PathVariable Long id,
                                 @RequestBody TypeEntreprise type) {

        TypeEntreprise existing =
                repository.findById(id).orElseThrow();

        existing.setTitre(type.getTitre());
        existing.setDescription(type.getDescription());

        return repository.save(existing);
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        // Vérifier si l’élément existe
        if (!repository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Type d’entreprise introuvable");
        }

        repository.deleteById(id);

        return ResponseEntity
                .ok("Type d’entreprise supprimé avec succès");
    }

}
