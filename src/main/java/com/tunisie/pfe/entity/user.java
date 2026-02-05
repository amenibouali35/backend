package com.tunisie.pfe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nom")
    private String nom;

    @NotBlank
    @Column(name = "prenom")
    private String prenom;

    @Email
    @NotBlank
    @Column(name = "email", unique = true, updatable = false)
    private String email;   // NE CHANGE JAMAIS

    @NotBlank
    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "photo_profil")
    private String photoProfil;

    @NotBlank
    @Column(name = "role")
    private String role;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    // 👉 Pour gérer les dates automatiquement
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }

    // ===== Constructeur vide =====
    public user() {}

    // ===== Getters & Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    // ❌ PAS DE setEmail (email ne change jamais)

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
}
