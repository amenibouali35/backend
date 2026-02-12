package com.tunisie.pfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tunisie.pfe.entity.user;
import com.tunisie.pfe.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ===== 1️⃣ Lire tous les utilisateurs =====
    public List<user> getAllUsers() {
        return userRepository.findByActive(1);
    }


    // ===== 2️⃣ Lire un utilisateur par ID =====
    public Optional<user> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // ===== 3️⃣ Créer un utilisateur =====
    public user createUser(user user) {

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        return userRepository.save(user);
    }

    // ===== 4️⃣ Modifier un utilisateur =====
    public user updateUser(Long id, user userDetails) {

        user existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        existingUser.setNom(userDetails.getNom());
        existingUser.setPrenom(userDetails.getPrenom());
        existingUser.setMotDePasse(userDetails.getMotDePasse());
        existingUser.setPhotoProfil(userDetails.getPhotoProfil());
        existingUser.setRole(userDetails.getRole());

        // ❌ Email NON modifiable
        // existingUser.setEmail(...);

        return userRepository.save(existingUser);
    }

    // ===== 5️⃣ Supprimer un utilisateur =====
    public void deleteUser(Long id) {
        user user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setActive(0); // désactiver
        userRepository.save(user);
    }


    // ===== 6️⃣ Chercher par email =====
    public Optional<user> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public void restoreUser(Long id) {
        user user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setActive(1);
        userRepository.save(user);
    }

}
