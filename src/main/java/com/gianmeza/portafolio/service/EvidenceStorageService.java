package com.gianmeza.portafolio.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gianmeza.portafolio.model.Evidence;

@Service
public class EvidenceStorageService {

    private final Path root;

    public EvidenceStorageService(@Value("${portfolio.storage-path:backed/evidencias}") String storagePath) {
        root = Path.of(storagePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
            IntStream.rangeClosed(1, 16).forEach(week -> {
                try {
                    Files.createDirectories(weekPath(week));
                } catch (IOException exception) {
                    throw new IllegalStateException("No se pudo crear el almacenamiento", exception);
                }
            });
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo preparar backed", exception);
        }
    }

    public List<Evidence.EvidenceFile> filesFor(int week) {
        try (var paths = Files.list(weekPath(week))) {
            return paths.filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .map(path -> new Evidence.EvidenceFile(path.getFileName().toString(), typeOf(path),
                            "/evidencias/archivo/" + week + "/" + path.getFileName(), iconOf(path), false))
                    .toList();
        } catch (IOException exception) {
            return List.of();
        }
    }

    public Evidence.EvidenceFile saveFile(int week, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Selecciona un archivo válido");
        }
        String safeName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
        Path destination = weekPath(week).resolve(safeName).normalize();
        if (!destination.getParent().equals(weekPath(week))) {
            throw new IllegalArgumentException("Nombre de archivo no válido");
        }
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
        }
        return new Evidence.EvidenceFile(safeName, typeOf(destination), "/evidencias/archivo/" + week + "/" + safeName,
                iconOf(destination), false);
    }

    public Evidence.EvidenceFile saveLink(int week, String name, String url) throws IOException {
        if (url == null || !(url.startsWith("https://") || url.startsWith("http://"))) {
            throw new IllegalArgumentException("El enlace debe comenzar con http:// o https://");
        }
        String safeName = (name == null || name.isBlank()) ? url : name.trim();
        Path links = weekPath(week).resolve("enlaces.txt");
        List<String> lines = Files.exists(links) ? Files.readAllLines(links) : new ArrayList<>();
        lines.add(safeName.replace("|", "-") + "|" + url);
        Files.write(links, lines);
        return new Evidence.EvidenceFile(safeName, "Enlace", url, "bi-link-45deg", true);
    }

    public List<Evidence.EvidenceFile> allFor(int week) {
        List<Evidence.EvidenceFile> result = new ArrayList<>(filesFor(week));
        Path links = weekPath(week).resolve("enlaces.txt");
        if (Files.exists(links)) {
            try {
                Files.readAllLines(links).stream().map(line -> line.split("\\|", 2))
                        .filter(parts -> parts.length == 2)
                        .forEach(parts -> result.add(new Evidence.EvidenceFile(parts[0], "Enlace", parts[1],
                                "bi-link-45deg", true)));
            } catch (IOException ignored) {
                // La evidencia sigue disponible aunque el índice de enlaces no pueda leerse.
            }
        }
        return result;
    }

    public Path resolveFile(int week, String filename) {
        Path file = weekPath(week).resolve(filename).normalize();
        return file.getParent().equals(weekPath(week)) && Files.isRegularFile(file) ? file : null;
    }

    private Path weekPath(int week) {
        if (week < 1 || week > 16) throw new IllegalArgumentException("Semana inválida");
        return root.resolve(String.format("semana-%02d", week));
    }

    private String typeOf(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".svg")) return "Imagen";
        if (name.endsWith(".pdf")) return "PDF";
        if (name.endsWith(".doc") || name.endsWith(".docx")) return "Documento";
        if (name.endsWith(".xls") || name.endsWith(".xlsx")) return "Hoja de cálculo";
        return "Archivo";
    }

    private String iconOf(Path path) {
        return switch (typeOf(path)) {
            case "Imagen" -> "bi-image";
            case "PDF" -> "bi-file-earmark-pdf";
            case "Documento" -> "bi-file-earmark-text";
            case "Hoja de cálculo" -> "bi-file-earmark-spreadsheet";
            default -> "bi-file-earmark-arrow-down";
        };
    }
}