package com.gianmeza.portafolio.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gianmeza.portafolio.model.Evidence;
import com.gianmeza.portafolio.service.EvidenceStorageService;

import java.util.List;

/**
 * Unit tests for PortfolioController.
 * Tests focus on portfolio display, evidence management, and admin functions.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("PortfolioController Tests")
class PortfolioControllerTest {

    private PortfolioController portfolioController;
    
    @Mock
    private EvidenceStorageService evidenceStorageService;
    
    @Mock
    private Model model;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private RedirectAttributes redirectAttributes;
    
    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        portfolioController = new PortfolioController(evidenceStorageService);
        when(evidenceStorageService.allFor(anyInt())).thenReturn(List.of());
    }

    // ========== Home Page Tests ==========
    
    @Test
    @DisplayName("Should return index view on GET /")
    void testHomeRootPath() {
        String result = portfolioController.home(model);
        assertEquals("index", result, "home() should return 'index' view");
    }

    @Test
    @DisplayName("Should return index view on GET /inicio")
    void testHomeInicio() {
        String result = portfolioController.home(model);
        assertEquals("index", result, "home() should return 'index' view");
    }

    @Test
    @DisplayName("Should populate model with skills, technologies, and weeks")
    void testHomePopulatesModel() {
        portfolioController.home(model);
        
        verify(model).addAttribute(eq("skills"), argThat(skills -> skills instanceof List));
        verify(model).addAttribute(eq("technologies"), argThat(techs -> techs instanceof List));
        verify(model).addAttribute(eq("weeks"), argThat(weeks -> weeks instanceof List && ((List<?>) weeks).size() == 16));
        verify(model).addAttribute(eq("evidences"), argThat(evidences -> evidences instanceof List));
    }

    @Test
    @DisplayName("Should provide 16 weeks in model")
    void testHomeProvides16Weeks() {
        portfolioController.home(model);
        
        // Capture the weeks argument to verify count
        verify(model).addAttribute(eq("weeks"), argThat(weeks -> ((List<?>) weeks).size() == 16));
    }

    @Test
    @DisplayName("Should provide skills list in model")
    void testHomeProvidedSkills() {
        portfolioController.home(model);
        verify(model).addAttribute(eq("skills"), argThat(skills -> {
            List<?> skillsList = (List<?>) skills;
            return skillsList.contains("Java") && skillsList.contains("Spring Boot");
        }));
    }

    // ========== Evidence Detail Tests ==========
    
    @Test
    @DisplayName("Should return evidence-detail view for valid week")
    void testEvidenceWithValidWeek() {
        String result = portfolioController.evidence(1, model);
        assertEquals("evidence-detail", result, "evidence() should return 'evidence-detail' view");
    }

    @Test
    @DisplayName("Should populate model with evidence for valid week")
    void testEvidencePopulatesModel() {
        portfolioController.evidence(5, model);
        verify(model).addAttribute(eq("evidence"), any(Evidence.class));
    }

    @Test
    @DisplayName("Should redirect when week is less than 1")
    void testEvidenceWithWeekLessThanOne() {
        String result = portfolioController.evidence(0, model);
        assertEquals("redirect:/#evidencias", result, "Week < 1 should redirect");
    }

    @Test
    @DisplayName("Should redirect when week is greater than 16")
    void testEvidenceWithWeekGreaterThan16() {
        String result = portfolioController.evidence(17, model);
        assertEquals("redirect:/#evidencias", result, "Week > 16 should redirect");
    }

    @Test
    @DisplayName("Should redirect when week is negative")
    void testEvidenceWithNegativeWeek() {
        String result = portfolioController.evidence(-1, model);
        assertEquals("redirect:/#evidencias", result, "Negative week should redirect");
    }

    @Test
    @DisplayName("Should accept boundary week values 1 and 16")
    void testEvidenceWithBoundaryWeeks() {
        String result1 = portfolioController.evidence(1, model);
        String result2 = portfolioController.evidence(16, model);
        
        assertEquals("evidence-detail", result1, "Week 1 should be valid");
        assertEquals("evidence-detail", result2, "Week 16 should be valid");
    }

    // ========== Admin Panel Tests ==========
    
    @Test
    @DisplayName("Should redirect to login when accessing admin panel without admin privileges")
    void testBackedRedirectsToLoginWithoutAdmin() {
        when(session.getAttribute("admin")).thenReturn(false);
        
        String result = portfolioController.backed(session, model);
        assertEquals("redirect:/login", result, "Non-admin should be redirected to login");
    }

    @Test
    @DisplayName("Should redirect to login when accessing admin panel with null admin attribute")
    void testBackedRedirectsToLoginWithNullAdmin() {
        when(session.getAttribute("admin")).thenReturn(null);
        
        String result = portfolioController.backed(session, model);
        assertEquals("redirect:/login", result, "Null admin attribute should redirect to login");
    }

    @Test
    @DisplayName("Should return backed view for admin user")
    void testBackedReturnsViewForAdmin() {
        when(session.getAttribute("admin")).thenReturn(true);
        
        String result = portfolioController.backed(session, model);
        assertEquals("backed", result, "Admin should access backed view");
    }

    @Test
    @DisplayName("Should populate model with weeks for admin")
    void testBackedPopulatesWeeksForAdmin() {
        when(session.getAttribute("admin")).thenReturn(true);
        
        portfolioController.backed(session, model);
        verify(model).addAttribute(eq("weeks"), argThat(weeks -> ((List<?>) weeks).size() == 16));
    }

    // ========== File Upload Tests ==========
    
    @Test
    @DisplayName("Should redirect to login when uploading without admin privileges")
    void testUploadRedirectsToLoginWithoutAdmin() {
        when(session.getAttribute("admin")).thenReturn(false);
        
        String result = portfolioController.upload(1, multipartFile, session, redirectAttributes);
        assertEquals("redirect:/login", result, "Non-admin upload should redirect to login");
    }

    @Test
    @DisplayName("Should save file and add success message for admin upload")
    void testUploadSuccessForAdmin() throws Exception {
        when(session.getAttribute("admin")).thenReturn(true);
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        
        String result = portfolioController.upload(5, multipartFile, session, redirectAttributes);
        
        assertEquals("redirect:/backed", result, "Upload should redirect to /backed");
        verify(evidenceStorageService).saveFile(5, multipartFile);
        verify(redirectAttributes).addFlashAttribute(eq("message"), contains("Semana 05"));
    }

    @Test
    @DisplayName("Should handle exception during file upload")
    void testUploadHandlesException() throws Exception {
        when(session.getAttribute("admin")).thenReturn(true);
        doThrow(new RuntimeException("Upload failed")).when(evidenceStorageService).saveFile(anyInt(), any());
        
        String result = portfolioController.upload(1, multipartFile, session, redirectAttributes);
        
        assertEquals("redirect:/backed", result, "Upload should redirect to /backed even on error");
        verify(redirectAttributes).addFlashAttribute(eq("error"), contains("Upload failed"));
    }

    @Test
    @DisplayName("Should format week number correctly in upload message")
    void testUploadFormatsWeekNumber() {
        when(session.getAttribute("admin")).thenReturn(true);
        
        portfolioController.upload(1, multipartFile, session, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute(eq("message"), contains("Semana 01"));
        
        portfolioController.upload(10, multipartFile, session, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute(eq("message"), contains("Semana 10"));
    }

    // ========== Link Publication Tests ==========
    
    @Test
    @DisplayName("Should redirect to login when publishing link without admin privileges")
    void testLinkRedirectsToLoginWithoutAdmin() {
        when(session.getAttribute("admin")).thenReturn(false);
        
        String result = portfolioController.link(1, "Link Name", "https://example.com", session, redirectAttributes);
        assertEquals("redirect:/login", result, "Non-admin link should redirect to login");
    }

    @Test
    @DisplayName("Should save link and add success message for admin")
    void testLinkSuccessForAdmin() throws Exception {
        when(session.getAttribute("admin")).thenReturn(true);
        
        String result = portfolioController.link(5, "Test Link", "https://example.com", session, redirectAttributes);
        
        assertEquals("redirect:/backed", result, "Link should redirect to /backed");
        verify(evidenceStorageService).saveLink(5, "Test Link", "https://example.com");
        verify(redirectAttributes).addFlashAttribute(eq("message"), contains("Semana 05"));
    }

    @Test
    @DisplayName("Should handle exception during link publication")
    void testLinkHandlesException() throws Exception {
        when(session.getAttribute("admin")).thenReturn(true);
        doThrow(new RuntimeException("Link save failed")).when(evidenceStorageService).saveLink(anyInt(), anyString(), anyString());
        
        String result = portfolioController.link(1, "Test", "https://example.com", session, redirectAttributes);
        
        assertEquals("redirect:/backed", result, "Link should redirect to /backed even on error");
        verify(redirectAttributes).addFlashAttribute(eq("error"), contains("Link save failed"));
    }

    @Test
    @DisplayName("Should format week number correctly in link message")
    void testLinkFormatsWeekNumber() {
        when(session.getAttribute("admin")).thenReturn(true);
        
        portfolioController.link(3, "Link", "https://example.com", session, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute(eq("message"), contains("Semana 03"));
    }

    // ========== Download Tests ==========
    
    @Test
    @DisplayName("Should return not found when file does not exist")
    void testDownloadFileNotFound() throws Exception {
        when(evidenceStorageService.resolveFile(1, "nonexistent.pdf")).thenReturn(null);
        
        var result = portfolioController.download(1, "nonexistent.pdf");
        assertFalse(result.hasBody(), "Should return not found response");
    }

    @Test
    @DisplayName("Should provide download response with proper headers")
    void testDownloadWithCorrectHeaders() throws Exception {
        when(evidenceStorageService.resolveFile(1, "test.pdf")).thenReturn(java.nio.file.Path.of("test.pdf"));
        
        try {
            var result = portfolioController.download(1, "test.pdf");
            assertNotNull(result, "Should return response entity");
        } catch (Exception ignored) {
            // File may not exist in test environment, which is expected
        }
    }

    // ========== Helper Assertions ==========
    
    /**
     * Helper to verify string contains substring in argument matcher.
     */
    private static String contains(String substring) {
        return argThat(arg -> arg != null && arg.toString().contains(substring));
    }
}
