package com.tunisie.pfe.controller;

import com.tunisie.pfe.entity.Lois;
import com.tunisie.pfe.repository.LoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lois")
@CrossOrigin("*")
public class LoiController {

    @Autowired
    private LoiRepository loiRepository;

    @GetMapping
    //http://localhost:8080/api/lois
    public List<Lois> getAll() {
        return loiRepository.findAll();
    }

    @PostMapping
    //http://localhost:8080/api/lois
    public Lois add(@RequestBody Lois loi) {
        loi.setDateCreation(LocalDateTime.now());
        return loiRepository.save(loi);
    }

    @PutMapping("/{id}")
    //http://localhost:8080/api/lois/1
    public Lois update(@PathVariable Long id, @RequestBody Lois loi) {
        Lois existing = loiRepository.findById(id).orElseThrow();
        existing.setTitre(loi.getTitre());
        existing.setContenu(loi.getContenu());
        existing.setDateModification(LocalDateTime.now());
        return loiRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    //http://localhost:8080/api/lois/2
    public void delete(@PathVariable Long id) {
        loiRepository.deleteById(id);
    }
}
