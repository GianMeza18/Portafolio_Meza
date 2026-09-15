package com.gianmeza.portafolio.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gianmeza.portafolio.model.Evidence;

@Service
public class EvidenceStorageService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EvidenceStorageService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Evidence.EvidenceFile> filesFor(int week) {
        return jdbcTemplate.query("SELECT nombre_archivo, tipo_archivo, icono FROM trabajos WHERE semana_id = ? ORDER BY nombre_archivo",
                (result, row) -> new Evidence.EvidenceFile(result.getString("nombre_archivo"),
                        typeOf(result.getString("nombre_archivo"), result.getString("tipo_archivo")),
                        "/evidencias/archivo/" + week + "/" + result.getString("nombre_archivo"),
                        result.getString("icono"), false), week);
    }

    public Evidence.EvidenceFile saveFile(int week, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Selecciona un archivo válido");
        }
        String safeName = safeName(file.getOriginalFilename());
        String contentType = file.getContentType() == null ? contentType(safeName) : file.getContentType();
        String icon = iconOf(safeName, contentType);
        jdbcTemplate.update("INSERT INTO trabajos (semana_id, nombre_archivo, tipo_archivo, contenido, tamano_bytes, icono) VALUES (?, ?, ?, ?, ?, ?) "
                        + "ON DUPLICATE KEY UPDATE tipo_archivo = VALUES(tipo_archivo), contenido = VALUES(contenido), tamano_bytes = VALUES(tamano_bytes), icono = VALUES(icono)",
                week, safeName, contentType, file.getBytes(), file.getSize(), icon);
        return new Evidence.EvidenceFile(safeName, typeOf(safeName, contentType), "/evidencias/archivo/" + week + "/" + safeName, icon, false);
    }

    public void replaceFiles(int week, MultipartFile[] files) throws IOException {
        jdbcTemplate.update("DELETE FROM trabajos WHERE semana_id = ?", week);
        if (files != null) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) saveFile(week, file);
            }
        }
    }

    public void saveDetails(int week, String title, String description) throws IOException {
        jdbcTemplate.update("UPDATE semanas SET titulo = ?, descripcion = ?, estado = 'COMPLETADA' WHERE id = ?",
                title == null ? "" : title.trim(), description == null ? "" : description.trim(), week);
    }

    public String titleFor(int week) {
        return detailFor(week, "titulo");
    }

    public String descriptionFor(int week) {
        return detailFor(week, "descripcion");
    }

    public Evidence.EvidenceFile saveLink(int week, String name, String url) throws IOException {
        if (url == null || !(url.startsWith("https://") || url.startsWith("http://"))) {
            throw new IllegalArgumentException("El enlace debe comenzar con http:// o https://");
        }
        String safeName = (name == null || name.isBlank()) ? url : name.trim();
        jdbcTemplate.update("INSERT INTO enlaces (semana_id, nombre, url) VALUES (?, ?, ?)", week, safeName.replace("|", "-"), url);
        return new Evidence.EvidenceFile(safeName, "Enlace", url, "bi-link-45deg", true);
    }

    public List<Evidence.EvidenceFile> allFor(int week) {
        List<Evidence.EvidenceFile> result = new ArrayList<>(filesFor(week));
        result.addAll(jdbcTemplate.query("SELECT nombre, url FROM enlaces WHERE semana_id = ? ORDER BY id",
                (resultSet, row) -> new Evidence.EvidenceFile(resultSet.getString("nombre"), "Enlace",
                        resultSet.getString("url"), "bi-link-45deg", true), week));
        return result;
    }

    public Path resolveFile(int week, String filename) {
        String safeName = safeName(filename);
        List<Path> files = jdbcTemplate.query("SELECT contenido FROM trabajos WHERE semana_id = ? AND nombre_archivo = ?",
                (result, row) -> {
                    try {
                        Path temporaryFile = Files.createTempFile("portafolio-", "-" + safeName);
                        Files.write(temporaryFile, result.getBytes("contenido"));
                        return temporaryFile;
                    } catch (IOException exception) {
                        throw new IllegalStateException("No se pudo preparar el archivo", exception);
                    }
                }, week, safeName);
        return files.isEmpty() ? null : files.get(0);
    }

    public String contentType(Path file) {
        try {
            String detected = Files.probeContentType(file);
            if (detected != null) return detected;
            String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
            if (name.endsWith(".png")) return "image/png";
            if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
            if (name.endsWith(".gif")) return "image/gif";
            if (name.endsWith(".svg")) return "image/svg+xml";
            if (name.endsWith(".pdf")) return "application/pdf";
            if (name.endsWith(".txt")) return "text/plain";
            if (name.endsWith(".html") || name.endsWith(".htm")) return "text/html";
            return "application/octet-stream";
        } catch (IOException exception) {
            return "application/octet-stream";
        }
    }

    private String detailFor(int week, String column) {
        List<String> values = jdbcTemplate.query("SELECT " + column + " FROM semanas WHERE id = ?",
                (result, row) -> result.getString(1), week);
        return values.isEmpty() ? "" : values.get(0);
    }

    private String safeName(String filename) {
        String safeName = filename == null ? "" : filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        if (safeName.isBlank() || safeName.equals(".") || safeName.equals("..")) {
            throw new IllegalArgumentException("Nombre de archivo no válido");
        }
        return safeName;
    }

    private String typeOf(String name, String contentType) {
        name = name.toLowerCase(Locale.ROOT);
        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".svg")) return "Imagen";
        if (name.endsWith(".pdf")) return "PDF";
        if (name.endsWith(".doc") || name.endsWith(".docx")) return "Documento";
        if (name.endsWith(".xls") || name.endsWith(".xlsx")) return "Hoja de cálculo";
        return contentType != null && contentType.startsWith("image/") ? "Imagen" : "Archivo";
    }

    private String iconOf(String name, String contentType) {
        return switch (typeOf(name, contentType)) {
            case "Imagen" -> "bi-image";
            case "PDF" -> "bi-file-earmark-pdf";
            case "Documento" -> "bi-file-earmark-text";
            case "Hoja de cálculo" -> "bi-file-earmark-spreadsheet";
            default -> "bi-file-earmark-arrow-down";
        };
    }

    private String contentType(String name) {
        String lowerName = name.toLowerCase(Locale.ROOT);
        if (lowerName.endsWith(".pdf")) return "application/pdf";
        if (lowerName.endsWith(".png")) return "image/png";
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) return "image/jpeg";
        if (lowerName.endsWith(".gif")) return "image/gif";
        if (lowerName.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
}