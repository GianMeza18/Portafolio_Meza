package com.gianmeza.portafolio.controller;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gianmeza.portafolio.model.UserProfile;

@Controller
public class AuthController {
    private static final String DEFAULT_PUBLIC_EMAIL = "vlecmanusa18@gmail.com";
    private static final String DEFAULT_PUBLIC_PASSWORD = "gianmeza";
    private static final String DEFAULT_PUBLIC_NAME = "Giancarlo Meza";
    private static final String DEFAULT_PUBLIC_PHOTO = "/img/mezafoto.jpeg";

    private final String adminEmail;
    private final String adminPassword;
    private final Path usersFile;
    private final JdbcTemplate jdbcTemplate;

    public AuthController(String adminEmail) {
        this(adminEmail, "gianmeza", "backed/usuarios.txt", null);
    }

    @Autowired
    public AuthController(
            @Value("${portfolio.admin-email:admin@gianmeza.com}") String adminEmail,
            @Value("${portfolio.admin-password:gianmeza}") String adminPassword,
            @Value("${portfolio.users-path:backed/usuarios.txt}") String usersPath,
            JdbcTemplate jdbcTemplate) {
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.usersFile = Path.of(usersPath).toAbsolutePath().normalize();
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @PostMapping("/login")
    public String authenticate(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();
        if (adminEmail.equalsIgnoreCase(normalizedEmail) && adminPassword.equals(password)) {
            session.setAttribute("admin", true);
            session.removeAttribute("user");
            return "redirect:/backed";
        }

        if (isPublicUser(normalizedEmail, password)) {
            ensurePublicUser();
            session.setAttribute("user", normalizedEmail);
            session.removeAttribute("admin");
            return "redirect:/";
        }

        if (registeredUser(normalizedEmail, password)) {
            session.setAttribute("user", normalizedEmail);
            session.removeAttribute("admin");
            return "redirect:/";
        }

        model.addAttribute("error", "Correo o contraseña incorrectos.");
        return "login";
    }

    @GetMapping("/registro")
    public String register() { return "register"; }

    @PostMapping("/registro")
    public String register(@RequestParam String email, @RequestParam String password, Model model) {
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();
        if (normalizedEmail.isBlank() || !normalizedEmail.contains("@") || password == null || password.length() < 6) {
            model.addAttribute("error", "Usa un correo válido y una contraseña de al menos 6 caracteres.");
            return "register";
        }
        if (jdbcTemplate != null) {
            try {
                Integer existing = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM usuarios WHERE email = ?", Integer.class, normalizedEmail);
                if (existing != null && existing > 0) {
                    model.addAttribute("error", "Ese correo ya está registrado.");
                } else {
                    jdbcTemplate.update("INSERT INTO usuarios (email, password_hash, nombre, rol) VALUES (?, ?, ?, 'USUARIO')",
                            normalizedEmail, hash(password), "Usuario");
                    model.addAttribute("message", "Registro completado. Ya puedes iniciar sesión con tu correo y contraseña.");
                }
            } catch (Exception exception) {
                model.addAttribute("error", "No se pudo completar el registro.");
            }
            return "register";
        }
        try {
            Files.createDirectories(usersFile.getParent());
            if (Files.exists(usersFile) && Files.readAllLines(usersFile).stream().anyMatch(line -> line.startsWith(normalizedEmail + "|"))) {
                model.addAttribute("error", "Ese correo ya está registrado.");
            } else {
                Files.writeString(usersFile, normalizedEmail + "|" + hash(password) + "|Usuario|/img/mezafoto.jpeg" + System.lineSeparator(),
                        StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
                model.addAttribute("message", "Registro completado. Ya puedes iniciar sesión con tu correo y contraseña.");
            }
        } catch (Exception exception) {
            model.addAttribute("error", "No se pudo completar el registro.");
        }
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "redirect:/login";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        String email = (String) session.getAttribute("user");
        if (email == null || email.isBlank()) {
            return "redirect:/login";
        }
        UserProfile profile = findUserProfile(email);
        if (profile == null) {
            if (DEFAULT_PUBLIC_EMAIL.equalsIgnoreCase(email)) {
                ensurePublicUser();
                profile = findUserProfile(email);
            }
        }
        model.addAttribute("userProfile", profile == null ? new UserProfile(email, "Usuario", "/img/mezafoto.jpeg") : profile);
        return "profile";
    }

    @PostMapping("/perfil")
    public String actualizarPerfil(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String password,
                                  @RequestParam(required = false, defaultValue = "/img/mezafoto.jpeg") String photoUrl,
                                  HttpSession session, Model model) {
        String currentEmail = (String) session.getAttribute("user");
        if (currentEmail == null || currentEmail.isBlank()) {
            return "redirect:/login";
        }

        UserProfile current = findUserProfile(currentEmail);
        if (current == null) {
            current = new UserProfile(currentEmail, DEFAULT_PUBLIC_NAME, DEFAULT_PUBLIC_PHOTO);
        }

        String newEmail = email == null || email.isBlank() ? current.email() : email.trim().toLowerCase();
        String newName = name == null || name.isBlank() ? current.name() : name.trim();
        String newPhoto = photoUrl == null || photoUrl.isBlank() ? current.photoUrl() : photoUrl.trim();
        String newPasswordHash = password == null || password.isBlank() ? current.passwordHash() : hash(password);

        updateUserProfile(current.email(), newEmail, newName, newPasswordHash, newPhoto);
        session.setAttribute("user", newEmail);
        model.addAttribute("userProfile", new UserProfile(newEmail, newName, newPhoto));
        return "profile";
    }

    private boolean isPublicUser(String email, String password) {
        return DEFAULT_PUBLIC_EMAIL.equalsIgnoreCase(email) && DEFAULT_PUBLIC_PASSWORD.equals(password);
    }

    private void ensurePublicUser() {
        if (jdbcTemplate != null) {
            jdbcTemplate.update("INSERT INTO usuarios (email, password_hash, nombre, rol) VALUES (?, ?, ?, 'USUARIO') "
                            + "ON DUPLICATE KEY UPDATE email = VALUES(email)",
                    DEFAULT_PUBLIC_EMAIL, hash(DEFAULT_PUBLIC_PASSWORD), DEFAULT_PUBLIC_NAME);
            return;
        }
        try {
            Files.createDirectories(usersFile.getParent());
            if (!Files.exists(usersFile) || Files.readAllLines(usersFile).stream().noneMatch(line -> line.startsWith(DEFAULT_PUBLIC_EMAIL + "|"))) {
                Files.writeString(usersFile, DEFAULT_PUBLIC_EMAIL + "|" + hash(DEFAULT_PUBLIC_PASSWORD) + "|" + DEFAULT_PUBLIC_NAME + "|" + DEFAULT_PUBLIC_PHOTO + System.lineSeparator(),
                        StandardCharsets.UTF_8,
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND);
            }
        } catch (Exception ignored) {
            // Se ignora para ofrecer acceso directo con la cuenta principal.
        }
    }

    private UserProfile findUserProfile(String email) {
        if (jdbcTemplate != null) {
            return jdbcTemplate.query("SELECT email, nombre, foto_url, password_hash FROM usuarios WHERE email = ?",
                    result -> result.next() ? new UserProfile(result.getString("email"), result.getString("nombre"),
                            result.getString("foto_url"), result.getString("password_hash")) : null, email);
        }
        try {
            if (!Files.exists(usersFile)) {
                return null;
            }
            return Files.readAllLines(usersFile).stream()
                    .map(line -> line.split("\\|", 4))
                    .filter(parts -> parts.length >= 2 && parts[0].equalsIgnoreCase(email))
                    .findFirst()
                    .map(parts -> new UserProfile(parts[0], parts.length >= 3 ? parts[2] : "Usuario", parts.length >= 4 ? parts[3] : "/img/mezafoto.jpeg", parts[1]))
                    .orElse(null);
        } catch (Exception exception) {
            return null;
        }
    }

    private void updateUserProfile(String currentEmail, String newEmail, String name, String passwordHash, String photoUrl) {
        if (jdbcTemplate != null) {
            jdbcTemplate.update("UPDATE usuarios SET email = ?, nombre = ?, password_hash = ?, foto_url = ? WHERE email = ?",
                    newEmail, name, passwordHash, photoUrl, currentEmail);
            return;
        }
        try {
            Files.createDirectories(usersFile.getParent());
            if (!Files.exists(usersFile)) {
                Files.writeString(usersFile, "", StandardCharsets.UTF_8);
            }
            var lines = Files.readAllLines(usersFile);
            var updated = new java.util.ArrayList<String>();
            for (String line : lines) {
                String[] parts = line.split("\\|", 4);
                if (parts.length >= 2 && parts[0].equalsIgnoreCase(currentEmail)) {
                    updated.add(newEmail + "|" + passwordHash + "|" + name + "|" + photoUrl);
                } else {
                    updated.add(line);
                }
            }
            Files.write(usersFile, updated, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo actualizar el perfil.", exception);
        }
    }

    private boolean registeredUser(String email, String password) {
        if (jdbcTemplate != null) {
            Integer matches = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM usuarios WHERE email = ? AND password_hash = ?",
                    Integer.class, email, hash(password));
            return matches != null && matches > 0;
        }
        try {
            if (!Files.exists(usersFile)) {
                return false;
            }
            return Files.readAllLines(usersFile).stream().anyMatch(line -> {
                String[] parts = line.split("\\|", 4);
                return parts.length >= 2 && parts[0].equalsIgnoreCase(email) && parts[1].equals(hash(password));
            });
        } catch (Exception exception) {
            return false;
        }
    }

    private String hash(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 no está disponible", exception);
        }
    }
}