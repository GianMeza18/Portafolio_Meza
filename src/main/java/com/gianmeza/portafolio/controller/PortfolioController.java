package com.gianmeza.portafolio.controller;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gianmeza.portafolio.model.Evidence;
import com.gianmeza.portafolio.service.EvidenceStorageService;

@Controller
public class PortfolioController {

    private static final List<String> SKILLS = List.of("Java", "Spring Boot", "HTML5", "CSS3", "JavaScript", "Bootstrap", "Git", "GitHub", "MySQL", "SQL Server");
    private static final List<String> TECHNOLOGIES = List.of("Java", "Spring Boot", "HTML5", "CSS3", "JavaScript", "Bootstrap", "MySQL", "Git", "GitHub", "VS Code", "IntelliJ IDEA");
    private static final List<Integer> WEEKS = IntStream.rangeClosed(1, 16).boxed().toList();

        private static final List<Evidence.EvidenceFile> WEEK_ONE_FILES = List.of(
            new Evidence.EvidenceFile("Infografía del proyecto", "Imagen", "/evidencias/semana-01/infografia.svg", "bi-image"),
            new Evidence.EvidenceFile("Resumen de fundamentos", "Documento", "/evidencias/semana-01/resumen-fundamentos.html", "bi-file-earmark-text"),
            new Evidence.EvidenceFile("Ficha de evidencias", "Documento", "/evidencias/semana-01/ficha-evidencia.txt", "bi-file-earmark-arrow-down"));

    private final EvidenceStorageService storage;

    public PortfolioController(EvidenceStorageService storage) {
        this.storage = storage;
    }

    @GetMapping({"/", "/inicio"})
    public String home(Model model) {
        model.addAttribute("skills", SKILLS);
        model.addAttribute("technologies", TECHNOLOGIES);
        model.addAttribute("weeks", WEEKS);
        model.addAttribute("evidences", WEEKS.stream().map(this::buildEvidence).toList());
        return "index";
    }

    @GetMapping("/evidencias/{week}")
    public String evidence(@PathVariable int week, Model model) {
        if (week < 1 || week > 16) {
            return "redirect:/#evidencias";
        }
        Evidence evidence = buildEvidence(week);
        model.addAttribute("evidence", evidence);
        return "evidence-detail";
    }

    @GetMapping("/evidencias/archivo/{week}/{filename:.+}")
    public ResponseEntity<Resource> download(@PathVariable int week, @PathVariable String filename) {
        Path file = storage.resolveFile(week, filename);
        if (file == null) return ResponseEntity.notFound().build();
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").body(resource);
    }

    @GetMapping("/backed")
    public String backed(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("weeks", WEEKS);
        return "backed";
    }

    @PostMapping("/backed/subir")
    public String upload(@RequestParam int week, @RequestParam MultipartFile file, HttpSession session, RedirectAttributes redirect) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            storage.saveFile(week, file);
            redirect.addFlashAttribute("message", "Archivo publicado en la Semana " + String.format("%02d", week) + ".");
        } catch (Exception exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/backed";
    }

    @PostMapping("/backed/enlace")
    public String link(@RequestParam int week, @RequestParam String name, @RequestParam String url, HttpSession session, RedirectAttributes redirect) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            storage.saveLink(week, name, url);
            redirect.addFlashAttribute("message", "Enlace publicado en la Semana " + String.format("%02d", week) + ".");
        } catch (Exception exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/backed";
    }

    private Evidence buildEvidence(int week) {
        List<Evidence.EvidenceFile> files = week == 1 ? new java.util.ArrayList<>(WEEK_ONE_FILES) : new java.util.ArrayList<>();
        files.addAll(storage.allFor(week));
        String image = files.stream().filter(file -> file.type().equals("Imagen")).map(Evidence.EvidenceFile::url).findFirst().orElse("/img/mezafoto.jpeg");
        boolean complete = !files.isEmpty();
        return new Evidence(week, week == 1 ? "Fundamentos de un Proyecto Web" : "Evidencia de la Semana " + String.format("%02d", week),
                complete ? "Archivos y enlaces disponibles para consultar o descargar." : "Esta página está preparada para publicar la actividad de la semana.",
                complete ? "Completada" : "Próximamente", image, files);
    }

    private boolean isAdmin(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("admin"));
    }
}
