# e-Portafolio Profesional

Portafolio digital de Gian Meza para el curso **Proyecto de Aplicación Profesional**. La aplicación está preparada para crecer semana a semana con nuevas evidencias, proyectos y aprendizajes.

## Tecnologías

- Java 21 y Spring Boot
- Maven y Thymeleaf
- HTML5, CSS3 y JavaScript
- Bootstrap 5, Bootstrap Icons y AOS
- Git y GitHub

## Instalación y ejecución

1. Requiere JDK 21 y Maven 3.9+.
2. Ejecuta `mvn spring-boot:run` desde la raíz.
3. Abre `http://localhost:8080`.

Para generar el artefacto ejecutable: `mvn clean package`.

## Estructura

- `src/main/java`: aplicación y controlador MVC.
- `src/main/resources/templates`: vistas Thymeleaf.
- `src/main/resources/static`: estilos y scripts del frontend.
- `backed/evidencias/semana-01` a `semana-16`: almacenamiento privado de trabajos publicados desde el panel.

## Panel privado de evidencias

1. Abre `http://localhost:8080/login`.
2. El acceso público permite registrarse con cualquier correo válido y contraseña.
3. El administrador privado entra con la cuenta configurada en `portfolio.admin-email` y la contraseña `gianmeza`.
4. En `/backed` selecciona una semana para subir archivos de cualquier formato o agregar enlaces.
5. Los trabajos publicados aparecen en la evidencia correspondiente y se entregan desde rutas controladas por la aplicación.

Los usuarios registrados solo pueden ver el portafolio. El panel privado queda reservado para la cuenta administrativa. Antes de desplegar el proyecto, cambia `portfolio.admin-email` y la contraseña del administrador por una configuración segura y externa.

## Autor

**Gian Meza** · Estudiante de Ingeniería de Sistemas · Desarrollo Web con Java

## Licencia

Proyecto académico personal. Todos los derechos reservados.
