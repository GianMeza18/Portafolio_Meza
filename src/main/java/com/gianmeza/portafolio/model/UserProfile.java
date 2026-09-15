package com.gianmeza.portafolio.model;

/**
 * Perfil de usuario que contiene información de cuenta y preferencias.
 */
public class UserProfile {
    private String email;
    private String name;
    private String photoUrl;
    private String passwordHash;

    public UserProfile(String email, String name, String photoUrl) {
        this(email, name, photoUrl, "");
    }

    public UserProfile(String email, String name, String photoUrl, String passwordHash) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // Métodos para acceder desde EL (Expression Language) en JSP
    public String email() {
        return email;
    }

    public String name() {
        return name;
    }

    public String photoUrl() {
        return photoUrl;
    }

    public String passwordHash() {
        return passwordHash;
    }
}
