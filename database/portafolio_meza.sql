-- Base de datos del e-Portafolio de Gian Meza
-- Compatible con MySQL/MariaDB de XAMPP y phpMyAdmin.

CREATE DATABASE IF NOT EXISTS portafolio_meza
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE portafolio_meza;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS enlaces;
DROP TABLE IF EXISTS trabajos;
DROP TABLE IF EXISTS semanas;
DROP TABLE IF EXISTS usuarios;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE usuarios (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    email VARCHAR(190) NOT NULL,
    password_hash CHAR(64) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    foto_url VARCHAR(500) NOT NULL DEFAULT '/img/mezafoto.jpeg',
    rol ENUM('USUARIO', 'ADMIN') NOT NULL DEFAULT 'USUARIO',
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuarios_email (email)
) ENGINE=InnoDB;

CREATE TABLE semanas (
    id TINYINT UNSIGNED NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT NOT NULL,
    estado ENUM('PROXIMAMENTE', 'COMPLETADA') NOT NULL DEFAULT 'PROXIMAMENTE',
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT ck_semanas_id CHECK (id BETWEEN 1 AND 16)
) ENGINE=InnoDB;

CREATE TABLE trabajos (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    semana_id TINYINT UNSIGNED NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    tipo_archivo VARCHAR(100) NOT NULL,
    contenido LONGBLOB NOT NULL,
    tamano_bytes BIGINT UNSIGNED NOT NULL,
    ruta_publica VARCHAR(500) DEFAULT NULL,
    icono VARCHAR(80) NOT NULL DEFAULT 'bi-file-earmark-arrow-down',
    subido_por BIGINT UNSIGNED DEFAULT NULL,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_trabajos_semana_nombre (semana_id, nombre_archivo),
    CONSTRAINT fk_trabajos_semana FOREIGN KEY (semana_id) REFERENCES semanas (id) ON DELETE CASCADE,
    CONSTRAINT fk_trabajos_usuario FOREIGN KEY (subido_por) REFERENCES usuarios (id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE enlaces (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    semana_id TINYINT UNSIGNED NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    url VARCHAR(2048) NOT NULL,
    creado_por BIGINT UNSIGNED DEFAULT NULL,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_enlaces_semana FOREIGN KEY (semana_id) REFERENCES semanas (id) ON DELETE CASCADE,
    CONSTRAINT fk_enlaces_usuario FOREIGN KEY (creado_por) REFERENCES usuarios (id) ON DELETE SET NULL
) ENGINE=InnoDB;

INSERT INTO semanas (id, titulo, descripcion, estado) VALUES
    (1, 'Fundamentos de un Proyecto Web', 'Infografía de Fundamentos de un Proyecto Web', 'COMPLETADA'),
    (2, 'Evidencia de la Semana 02', 'Actividad completada. Archivos y enlaces disponibles para consultar o descargar.', 'COMPLETADA'),
    (3, 'Evidencia de la Semana 03', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (4, 'Evidencia de la Semana 04', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (5, 'Evidencia de la Semana 05', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (6, 'Evidencia de la Semana 06', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (7, 'Evidencia de la Semana 07', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (8, 'Evidencia de la Semana 08', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (9, 'Evidencia de la Semana 09', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (10, 'Evidencia de la Semana 10', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (11, 'Evidencia de la Semana 11', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (12, 'Evidencia de la Semana 12', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (13, 'Evidencia de la Semana 13', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (14, 'Evidencia de la Semana 14', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (15, 'Evidencia de la Semana 15', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE'),
    (16, 'Evidencia de la Semana 16', 'Esta página está preparada para publicar la actividad de la semana.', 'PROXIMAMENTE');

-- Usuario administrador inicial: cambia la contraseña después de importar.
-- Hash SHA-256 de "gianmeza".
INSERT INTO usuarios (email, password_hash, nombre, rol)
VALUES ('admin@gianmeza.com', SHA2('gianmeza', 256), 'Administrador', 'ADMIN');

-- Para cargar un trabajo desde phpMyAdmin, usa un INSERT como este:
-- INSERT INTO trabajos (semana_id, nombre_archivo, tipo_archivo, contenido, tamano_bytes, icono)
-- VALUES (1, 'mi-trabajo.pdf', 'application/pdf', LOAD_FILE('C:/xampp/htdocs/mi-trabajo.pdf'), 0, 'bi-file-earmark-pdf');
-- Es más sencillo subir los binarios desde el formulario de la aplicación una vez conectada a MySQL.
