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
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gianmeza.portafolio.model.Evidence;
import com.gianmeza.portafolio.service.EvidenceStorageService;

@Controller
public class PortfolioController {

    private static final List<String> SKILLS = List.of("Java", "Spring Boot", "HTML5", "CSS3", "JavaScript", "Bootstrap", "Git", "GitHub", "MySQL", "SQL Server");
    private static final List<String> TECHNOLOGIES = List.of("Java", "Spring Boot", "HTML5", "CSS3", "JavaScript", "Bootstrap", "MySQL", "Git", "GitHub", "VS Code", "IntelliJ IDEA");
    private static final List<Integer> WEEKS = IntStream.rangeClosed(1, 16).boxed().toList();

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
    @GetMapping("/evidencias/ver/{week}/{filename:.+}")
public ResponseEntity<Resource> viewFile(
        @PathVariable int week,
        @PathVariable String filename) {

    Path file = storage.resolveFile(week, filename);

    if (file == null) {
        return ResponseEntity.notFound().build();
    }

    Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(storage.contentType(file)))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=\"" + filename + "\"")
            .body(resource);
}

    @GetMapping("/backed")
    public String backed(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("weeks", WEEKS);
        return "backed";
    }

    @GetMapping("/backed/actualizar/{week}")
    public String editEvidence(@PathVariable int week, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        if (week < 1 || week > 16) return "redirect:/backed";
        model.addAttribute("evidence", buildEvidence(week));
        return "evidence-edit";
    }

    @PostMapping("/backed/actualizar/{week}")
    public String updateEvidence(
            @PathVariable int week,
            @RequestParam String title,
            @RequestParam(required = false, defaultValue = "") String description,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            HttpSession session,
            RedirectAttributes redirect) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            storage.saveDetails(week, title, description);
            if (files != null && java.util.Arrays.stream(files).anyMatch(file -> file != null && !file.isEmpty())) {
                storage.replaceFiles(week, files);
            }
            redirect.addFlashAttribute("message", "Semana " + String.format("%02d", week) + " actualizada correctamente.");
        } catch (Exception exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/backed";
    }

    @PostMapping("/backed/subir")
public String upload(
        @RequestParam int week,
        @RequestParam("files") MultipartFile[] files,
        @RequestParam String title,
        @RequestParam(required = false, defaultValue = "") String description,
        HttpSession session,
        RedirectAttributes redirect
) {

    if (!isAdmin(session)) {
        return "redirect:/login";
    }

    try {

        storage.saveDetails(week, title, description);

        for (MultipartFile file : files) {

            if (!file.isEmpty()) {
                storage.saveFile(week, file);
            }

        }

        redirect.addFlashAttribute(
                "message",
                files.length + 
                " archivos publicados en la Semana " 
                + String.format("%02d", week)
        );

    } catch (Exception exception) {

        redirect.addFlashAttribute(
                "error",
                exception.getMessage()
        );

    }

    return "redirect:/backed";
}

    public String upload(int week, MultipartFile file, HttpSession session, RedirectAttributes redirect) {
        return upload(week, new MultipartFile[] { file }, "Trabajo de la Semana " + String.format("%02d", week), "", session, redirect);
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
        List<Evidence.EvidenceFile> files = new java.util.ArrayList<>(storage.allFor(week));
        String image = files.stream().filter(file -> file.type().equals("Imagen")).map(file -> file.external()
            ? file.url()
            : file.url().replace("/evidencias/archivo/", "/evidencias/ver/")).findFirst().orElse("/img/mezafoto.jpeg");
        boolean complete = !files.isEmpty();
        String storedTitle = java.util.Objects.requireNonNullElse(storage.titleFor(week), "");
        String storedDescription = java.util.Objects.requireNonNullElse(storage.descriptionFor(week), "");
        String title = storedTitle.isBlank() ? (week == 1 ? "Fundamentos de un Proyecto Web" : "Evidencia de la Semana " + String.format("%02d", week)) : storedTitle;
        String description = storedDescription.isBlank()
            ? (complete || week == 2 ? "Actividad completada. Archivos y enlaces disponibles para consultar o descargar." : "Esta página está preparada para publicar la actividad de la semana.")
            : storedDescription;
        boolean completed = week == 1 || week == 2 || complete;
        return new Evidence(week, title, description, completed ? "Completada" : "Próximamente", image, files);
    }

    private boolean isAdmin(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("admin"));
    }
}
