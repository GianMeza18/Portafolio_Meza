package com.gianmeza.portafolio.controller;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import com.gianmeza.portafolio.model.Evidence;

@Controller
public class PortfolioController {

    private static final List<String> SKILLS = List.of("Java", "Spring Boot", "HTML5", "CSS3", "JavaScript", "Bootstrap", "Git", "GitHub", "MySQL", "SQL Server");
    private static final List<String> TECHNOLOGIES = List.of("Java", "Spring Boot", "HTML5", "CSS3", "JavaScript", "Bootstrap", "MySQL", "Git", "GitHub", "VS Code", "IntelliJ IDEA");
    private static final List<Integer> WEEKS = IntStream.rangeClosed(1, 16).boxed().toList();

        private static final List<Evidence.EvidenceFile> WEEK_ONE_FILES = List.of(
            new Evidence.EvidenceFile("Infografía del proyecto", "Imagen", "/evidencias/semana-01/infografia.svg", "bi-image"),
            new Evidence.EvidenceFile("Resumen de fundamentos", "Documento", "/evidencias/semana-01/resumen-fundamentos.html", "bi-file-earmark-text"),
            new Evidence.EvidenceFile("Ficha de evidencias", "Documento", "/evidencias/semana-01/ficha-evidencia.txt", "bi-file-earmark-arrow-down"));

    @GetMapping({"/", "/inicio"})
    public String home(Model model) {
        model.addAttribute("skills", SKILLS);
        model.addAttribute("technologies", TECHNOLOGIES);
        model.addAttribute("weeks", WEEKS);
        return "index";
    }

    @GetMapping("/evidencias/{week}")
    public String evidence(@PathVariable int week, Model model) {
        if (week < 1 || week > 16) {
            return "redirect:/#evidencias";
        }
        Evidence evidence = week == 1
                ? new Evidence(1, "Fundamentos de un Proyecto Web", "Infografía, resumen, repositorio GitHub y portafolio web.", "Completada", "/evidencias/semana-01/infografia.svg", WEEK_ONE_FILES)
                : new Evidence(week, "Evidencia de la Semana " + String.format("%02d", week), "Esta página está preparada para publicar la actividad, documentos, imágenes y enlaces de la semana.", "Próximamente", "/img/mezafoto.jpeg", List.of());
        model.addAttribute("evidence", evidence);
        return "evidence-detail";
    }
}
