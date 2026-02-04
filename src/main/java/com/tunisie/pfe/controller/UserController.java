package com.tunisie.pfe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tunisie.pfe.entity.user;
import com.tunisie.pfe.service.UserService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Pour autoriser React à accéder à l'API
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // ========== API 1 : LIRE TOUS LES USERS (GET) ==========
    // URL: http://localhost:8080/api/users
    @GetMapping
    public ResponseEntity<List<user>> getAllUsers() {
        List<user> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    // ========== API 2 : LIRE UN USER PAR ID (GET) ==========
    // URL: http://localhost:8080/api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<user> getUserById(@PathVariable Long id) {
        user user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    // ========== API 3 : AJOUTER UN NOUVEAU USER (POST) ==========
    // URL: http://localhost:8080/api/users
    @PostMapping
    public ResponseEntity<user> createUser(@Valid @RequestBody user user) {
        user newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    
    // ========== API 4 : MODIFIER UN USER EXISTANT (PUT) ==========
    // URL: http://localhost:8080/api/users/1
    @PutMapping("/{id}")
    public ResponseEntity<user> updateUser(@PathVariable Long id, @Valid @RequestBody user userDetails) {
        user updatedUser = userService.updateUser(id, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    // ========== API 5 : SUPPRIMER UN USER (DELETE) ==========
    // URL: http://localhost:8080/api/users/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("Utilisateur supprimé avec succès", HttpStatus.OK);
    }
    
    // ========== API BONUS : Chercher par email (GET) ==========
    // URL: http://localhost:8080/api/users/email/test@example.com
    @GetMapping("/email/{email}")
    public ResponseEntity<user> getUserByEmail(@PathVariable String email) {
        user user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    // ========== API BONUS : Chercher par CIN (GET) ==========
    // URL: http://localhost:8080/api/users/cin/12345678
    @GetMapping("/cin/{cin}")
    public ResponseEntity<user> getUserByCin(@PathVariable String cin) {
        user user = userService.getUserByCin(cin)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}