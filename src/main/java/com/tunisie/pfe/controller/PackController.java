package com.tunisie.pfe.controller;

import com.tunisie.pfe.entity.Pack;
import com.tunisie.pfe.repository.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packs")
@CrossOrigin("*")
public class PackController {

    @Autowired
    private PackRepository repository;

    // 🔹 GET ALL
    @GetMapping
    public List<Pack> getAll() {
        return repository.findAll();
    }

    // 🔹 POST
    @PostMapping
    public ResponseEntity<String> add(@RequestBody Pack pack) {
        repository.save(pack);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Pack ajouté avec succès");
    }

    // 🔹 PUT
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestBody Pack pack) {

        Pack existing = repository.findById(id).orElseThrow();

        existing.setTitre(pack.getTitre());
        existing.setDescription(pack.getDescription());

        repository.save(existing);

        return ResponseEntity.ok("Pack modifié avec succès");
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Pack introuvable");
        }

        repository.deleteById(id);
        return ResponseEntity.ok("Pack supprimé avec succès");
    }
}
