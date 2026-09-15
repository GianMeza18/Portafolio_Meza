INSERT IGNORE INTO semanas (id, titulo, descripcion, estado) VALUES
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

INSERT IGNORE INTO usuarios (email, password_hash, nombre, rol)
VALUES ('admin@gianmeza.com', SHA2('gianmeza', 256), 'Administrador', 'ADMIN');
