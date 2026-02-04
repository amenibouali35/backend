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
    
    // ========== MÉTHODE 1 : LIRE TOUS LES USERS ==========
    public List<user> getAllUsers() {
        return userRepository.findAll();
    }
    
    // ========== MÉTHODE 2 : LIRE UN USER PAR ID ==========
    public Optional<user> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    // ========== MÉTHODE 3 : AJOUTER UN NOUVEAU USER ==========
    public user createUser(user user) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        // Vérifier si le CIN existe déjà
        if (userRepository.existsByCin(user.getCin())) {
            throw new RuntimeException("Un utilisateur avec ce CIN existe déjà");
        }
        return userRepository.save(user);
    }
    
    // ========== MÉTHODE 4 : MODIFIER UN USER EXISTANT ==========
    public user updateUser(Long id, user userDetails) {
        // Chercher le user par ID
        user user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
        
        // Mettre à jour les champs
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setCin(userDetails.getCin());
        
        // Sauvegarder les modifications
        return userRepository.save(user);
    }
    
    // ========== MÉTHODE 5 : SUPPRIMER UN USER ==========
    public void deleteUser(Long id) {
        // Vérifier si le user existe
        user user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
        
        // Supprimer le user
        userRepository.delete(user);
    }
    
    // ========== MÉTHODE BONUS : Chercher par email ==========
    public Optional<user> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // ========== MÉTHODE BONUS : Chercher par CIN ==========
    public Optional<user> getUserByCin(String cin) {
        return userRepository.findByCin(cin);
    }
}