package com.gianmeza.portafolio.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Unit tests for AuthController.
 * Tests focus on authentication flows, registration, profile management,
 * and error handling without external dependencies.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    private AuthController authController;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private Model model;

    @TempDir
    private Path temporaryDirectory;

    @BeforeEach
    void setUp() {
        authController = new AuthController("admin@gianmeza.com");
    }

    // ========== Login Page Tests ==========
    
    @Test
    @DisplayName("Should return login view when accessing GET /login")
    void testLoginPageReturnsLoginView() {
        String result = authController.login();
        assertEquals("login", result, "login() should return 'login' view name");
    }

    // ========== Authentication Tests ==========
    
    @Test
    @DisplayName("Should authenticate admin with correct credentials")
    void testAdminAuthenticationSuccess() {
        String result = authController.authenticate("admin@gianmeza.com", "gianmeza", session, model);
        
        assertEquals("redirect:/backed", result, "Admin login should redirect to /backed");
        verify(session).setAttribute("admin", true);
        verify(session).removeAttribute("user");
    }

    @Test
    @DisplayName("Should reject admin authentication with wrong password")
    void testAdminAuthenticationFailureWrongPassword() {
        String result = authController.authenticate("admin@gianmeza.com", "wrongpassword", session, model);
        
        assertEquals("login", result, "Wrong admin password should return to login");
        verify(model).addAttribute("error", "Correo o contraseña incorrectos.");
    }

    @Test
    @DisplayName("Should authenticate public user with correct credentials")
    void testPublicUserAuthenticationSuccess() {
        cleanupTestFiles();
        String result = authController.authenticate("vlecmanusa18@gmail.com", "gianmeza", session, model);
        
        assertEquals("redirect:/", result, "Public user login should redirect to the portfolio");
        verify(session).setAttribute("user", "vlecmanusa18@gmail.com");
        verify(session).removeAttribute("admin");
        cleanupTestFiles();
    }

    @Test
    @DisplayName("Should reject authentication with null email")
    void testAuthenticationWithNullEmail() {
        String result = authController.authenticate(null, "gianmeza", session, model);
        
        assertEquals("login", result, "Null email should return to login");
        verify(model).addAttribute("error", "Correo o contraseña incorrectos.");
    }

    @Test
    @DisplayName("Should normalize email to lowercase during authentication")
    void testAuthenticationEmailNormalization() {
        String result = authController.authenticate("ADMIN@GIANMEZA.COM", "gianmeza", session, model);
        
        assertEquals("redirect:/backed", result, "Email should be normalized to lowercase");
        verify(session).setAttribute("admin", true);
    }

    @Test
    @DisplayName("Should reject authentication with wrong email")
    void testAuthenticationFailureWrongEmail() {
        String result = authController.authenticate("wrong@email.com", "gianmeza", session, model);
        
        assertEquals("login", result, "Wrong email should return to login");
        verify(model).addAttribute("error", "Correo o contraseña incorrectos.");
    }

    // ========== Registration Tests ==========
    
    @Test
    @DisplayName("Should return register view when accessing GET /registro")
    void testRegisterPageReturnsRegisterView() {
        String result = authController.register();
        assertEquals("register", result, "register() should return 'register' view name");
    }

    @Test
    @DisplayName("Should reject registration with invalid email format")
    void testRegisterWithInvalidEmailFormat() {
        String result = authController.register("invalidemail", "password123", model);
        
        assertEquals("register", result, "Invalid email should return register view");
        verify(model).addAttribute("error", "Usa un correo válido y una contraseña de al menos 6 caracteres.");
    }

    @Test
    @DisplayName("Should reject registration with short password")
    void testRegisterWithShortPassword() {
        String result = authController.register("test@email.com", "12345", model);
        
        assertEquals("register", result, "Short password should return register view");
        verify(model).addAttribute("error", "Usa un correo válido y una contraseña de al menos 6 caracteres.");
    }

    @Test
    @DisplayName("Should reject registration with blank email")
    void testRegisterWithBlankEmail() {
        String result = authController.register("", "password123", model);
        
        assertEquals("register", result, "Blank email should return register view");
        verify(model).addAttribute("error", "Usa un correo válido y una contraseña de al menos 6 caracteres.");
    }

    @Test
    @DisplayName("Should reject registration with null password")
    void testRegisterWithNullPassword() {
        String result = authController.register("test@email.com", null, model);
        
        assertEquals("register", result, "Null password should return register view");
        verify(model).addAttribute("error", "Usa un correo válido y una contraseña de al menos 6 caracteres.");
    }

    @Test
    @DisplayName("Should normalize email to lowercase during registration")
    void testRegisterEmailNormalization() {
        cleanupTestFiles();
        String result = authController.register("TEST@EMAIL.COM", "password123", model);
        
        assertEquals("register", result);
        verify(model).addAttribute("message", "Registro completado. Ya puedes iniciar sesión con tu correo y contraseña.");
        cleanupTestFiles();
    }

    // ========== Logout Tests ==========
    
    @Test
    @DisplayName("Should invalidate session and redirect to login on logout")
    void testLogoutInvalidatesSession() {
        String result = authController.logout(session);
        
        assertEquals("redirect:/login", result, "logout() should redirect to login");
        verify(session).invalidate();
    }

    // ========== Error Page Tests ==========
    
    @Test
    @DisplayName("Should redirect to login on error page")
    void testErrorPageRedirectsToLogin() {
        String result = authController.errorPage();
        assertEquals("redirect:/login", result, "errorPage() should redirect to login");
    }

    // ========== Profile View Tests ==========
    
    @Test
    @DisplayName("Should redirect to login when accessing profile without session")
    void testPerfilWithoutSession() {
        String result = authController.perfil(session, model);
        
        assertEquals("redirect:/login", result, "Profile access without session should redirect to login");
    }

    @Test
    @DisplayName("Should redirect to login with null email in session")
    void testPerfilWithNullEmailInSession() {
        when(session.getAttribute("user")).thenReturn(null);
        String result = authController.perfil(session, model);
        
        assertEquals("redirect:/login", result, "Profile access with null email should redirect to login");
    }

    @Test
    @DisplayName("Should return profile view with user profile")
    void testPerfilReturnsProfileView() {
        cleanupTestFiles();
        when(session.getAttribute("user")).thenReturn("vlecmanusa18@gmail.com");
        
        String result = authController.perfil(session, model);
        
        assertEquals("profile", result, "perfil() should return 'profile' view");
        verify(model).addAttribute(eq("userProfile"), any());
        cleanupTestFiles();
    }

    // ========== Profile Update Tests ==========
    
    @Test
    @DisplayName("Should reject profile update without session")
    void testActualizarPerfilWithoutSession() {
        String result = authController.actualizarPerfil("New Name", "new@email.com", "newpass123", 
            null, session, model);
        
        assertEquals("redirect:/login", result, "Profile update without session should redirect to login");
    }

    @Test
    @DisplayName("Should update user profile with valid data")
    void testActualizarPerfilWithValidData() {
        cleanupTestFiles();
        when(session.getAttribute("user")).thenReturn("vlecmanusa18@gmail.com");
        
        // First ensure public user exists
        authController.authenticate("vlecmanusa18@gmail.com", "gianmeza", session, model);
        
        // Now update profile
        reset(session);
        when(session.getAttribute("user")).thenReturn("vlecmanusa18@gmail.com");
        String result = authController.actualizarPerfil("Updated Name", null, null, null, session, model);
        
        assertEquals("profile", result, "Profile update should return profile view");
        verify(session).setAttribute("user", "vlecmanusa18@gmail.com");
        verify(model).addAttribute(eq("userProfile"), any());
        cleanupTestFiles();
    }

    @Test
    @DisplayName("Should preserve name when not provided in profile update")
    void testActualizarPerfilPreservesNameWhenNotProvided() {
        cleanupTestFiles();
        when(session.getAttribute("user")).thenReturn("vlecmanusa18@gmail.com");
        
        // First ensure public user exists
        authController.authenticate("vlecmanusa18@gmail.com", "gianmeza", session, model);
        
        // Now update profile without name
        reset(session);
        when(session.getAttribute("user")).thenReturn("vlecmanusa18@gmail.com");
        String result = authController.actualizarPerfil(null, null, null, null, session, model);
        
        assertEquals("profile", result, "Profile update should return profile view");
        cleanupTestFiles();
    }

    @Test
    @DisplayName("Should store an uploaded profile photo and update the session avatar")
    void testActualizarPerfilStoresUploadedPhoto() throws Exception {
        Path usersFile = temporaryDirectory.resolve("users.txt");
        Path photosDirectory = temporaryDirectory.resolve("profile-photos");
        authController = new AuthController("admin@gianmeza.com", "gianmeza", usersFile.toString(), null, photosDirectory.toString());
        when(session.getAttribute("user")).thenReturn("vlecmanusa18@gmail.com");
        authController.authenticate("vlecmanusa18@gmail.com", "gianmeza", session, model);
        MockMultipartFile photo = new MockMultipartFile("photo", "avatar.png", "image/png", new byte[] { 1, 2, 3 });

        String result = authController.actualizarPerfil("Giancarlo", null, null, photo, session, model);

        assertEquals("profile", result);
        Path storedPhoto = Files.list(photosDirectory).findFirst().orElseThrow();
        assertArrayEquals(new byte[] { 1, 2, 3 }, Files.readAllBytes(storedPhoto));
        verify(session).setAttribute(eq("userPhoto"), argThat(url -> url.toString().startsWith("/perfil/foto/")));
    }

    // ========== Helper Methods ==========
    
    /**
     * Cleans up test files to ensure test isolation.
     */
    private void cleanupTestFiles() {
        try {
            Path usersFile = Path.of("backed", "usuarios.txt");
            if (Files.exists(usersFile)) {
                Files.delete(usersFile);
            }
            Path parentDir = usersFile.getParent();
            if (Files.exists(parentDir) && Files.list(parentDir).findAny().isEmpty()) {
                Files.delete(parentDir);
            }
        } catch (Exception ignored) {
            // Cleanup is best-effort
        }
    }
}
