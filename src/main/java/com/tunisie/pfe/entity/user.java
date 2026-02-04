package com.tunisie.pfe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class user {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    @Column(name = "name")
    private String name;
    
    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(name = "email", unique = true)
    private String email;
    
    @NotBlank(message = "Le rôle est obligatoire")
    @Column(name = "role")
    private String role;
    
    @NotBlank(message = "Le CIN est obligatoire")
    @Column(name = "cin", unique = true)
    private String cin;
    
    // Constructeur vide (obligatoire pour JPA)
    public user() {
    }
    
    // Constructeur avec paramètres
    public user(String name, String email, String role, String cin) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.cin = cin;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getCin() {
        return cin;
    }
    
    public void setCin(String cin) {
        this.cin = cin;
    }
}