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
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // ===== 1️⃣ GET : tous les utilisateurs =====
    //http://localhost:8080/api/users
    @GetMapping
    public ResponseEntity<List<user>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // ===== 2️⃣ GET : utilisateur par ID =====
    //http://localhost:8080/api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<user> getUserById(@PathVariable Long id) {
        user user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // ===== 3️⃣ POST : créer un utilisateur =====
   
    //http://localhost:8080/api/users
    @PostMapping
    public ResponseEntity<user> createUser(@Valid @RequestBody user user) {
        user newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // ===== 4️⃣ PUT : modifier un utilisateur =====
    //http://localhost:8080/api/users/1
    @PutMapping("/{id}")
    public ResponseEntity<user> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody user userDetails) {

        user updatedUser = userService.updateUser(id, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // ===== 5️⃣ DELETE : supprimer un utilisateur =====
    //http://localhost:8080/api/users/3
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("Utilisateur supprimé avec succès", HttpStatus.OK);
    }

    // ===== 6️⃣ GET : chercher par email =====
    //http://localhost:8080/api/users/email/souha@gmail.com
    @GetMapping("/email/{email}")
    public ResponseEntity<user> getUserByEmail(@PathVariable String email) {
        user user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
