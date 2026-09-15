# e-Portafolio Profesional

Portafolio digital de Gian Meza para el curso **Proyecto de Aplicación Profesional**. La aplicación está preparada para crecer semana a semana con nuevas evidencias, proyectos y aprendizajes.

## Tecnologías

- Java 24 y Spring Boot 3.4
- Maven, JSP y JSTL
- CSS y JavaScript para la interfaz
- Bootstrap Icons y AOS
- Git y GitHub

## Instalación y ejecución

1. Requiere JDK 24 y Maven 3.9+.
2. Ejecuta `mvn spring-boot:run` desde la raíz.
3. Abre `http://localhost:8080`.

Para generar el artefacto ejecutable: `mvn clean package`.

## Base de datos MySQL en XAMPP

El script [database/portafolio_meza.sql](database/portafolio_meza.sql) crea la base `portafolio_meza` y las tablas `usuarios`, `semanas`, `trabajos` y `enlaces`. La aplicación usa JDBC para leer y guardar estos datos. Para importarlo en phpMyAdmin:

1. Inicia **Apache** y **MySQL** desde XAMPP.
2. Entra a `http://localhost/phpmyadmin`.
3. Selecciona **Importar**, elige `database/portafolio_meza.sql` y pulsa **Continuar**.
4. Comprueba que aparezca la base `portafolio_meza` con sus cuatro tablas.

La tabla `trabajos` guarda los archivos en la columna `contenido` (`LONGBLOB`). El formulario de administración los inserta directamente en MySQL.

Para ejecutar localmente con XAMPP, inicia MySQL en el puerto `3306` y ejecuta `mvn spring-boot:run`. La configuración predeterminada usa el usuario `root` sin contraseña.

Si MySQL muestra `Packet for query is too large`, configura `max_allowed_packet=64M` en `D:/xampp/mysql/bin/my.ini`, tanto en `[mysqld]` como en `[mysqldump]`, y reinicia MySQL desde XAMPP. Esta aplicación permite archivos de hasta `50MB`.

Para Render, configura `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` con una base MySQL/MariaDB alojada externamente. No uses `localhost` en `DB_URL`, porque desde Render `localhost` apunta al contenedor de la aplicación, no a tu PC.

## Despliegue en Render con Docker

El repositorio incluye un `Dockerfile` multi-etapa y `render.yaml`. Para desplegarlo:

1. Sube el repositorio a GitHub y crea un **Blueprint** en Render apuntando a ese repositorio.
2. Render detectará `render.yaml`, construirá la imagen y asignará el puerto mediante `PORT`.
3. Define `PORTFOLIO_ADMIN_EMAIL`, `PORTFOLIO_ADMIN_PASSWORD`, `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` como variables secretas en Render.
4. Haz el primer despliegue y abre la URL pública que Render asigne.

Los usuarios, evidencias y enlaces nuevos se almacenan en MySQL. El servicio de base de datos debe permitir conexiones desde Render y tener copias de seguridad. La cuenta administrativa usa `PORTFOLIO_ADMIN_EMAIL` y `PORTFOLIO_ADMIN_PASSWORD`.

Para probar la imagen localmente:

```bash
docker build -t portafolio-meza .
docker run --rm -p 8080:8080 -e PORTFOLIO_ADMIN_PASSWORD=cambia-esta-clave portafolio-meza
```

## Estructura

- `src/main/java`: aplicación y controlador MVC.
- `src/main/webapp/WEB-INF/views`: vistas JSP protegidas.
- `src/main/resources/static`: estilos y scripts del frontend.
- `backed/evidencias/semana-01` a `semana-16`: almacenamiento privado de trabajos publicados desde el panel.

## Panel privado de evidencias

1. Abre `http://localhost:8080/login`.
2. El acceso público permite registrarse con cualquier correo válido y contraseña.
3. El administrador privado entra con la cuenta configurada en `PORTFOLIO_ADMIN_EMAIL` y la contraseña definida en `PORTFOLIO_ADMIN_PASSWORD`.
4. En `/backed` selecciona una semana para subir archivos de cualquier formato o agregar enlaces.
5. Los trabajos publicados aparecen en la evidencia correspondiente y se entregan desde rutas controladas por la aplicación.

Los usuarios registrados solo pueden ver el portafolio. El panel privado queda reservado para la cuenta administrativa. Antes de desplegar el proyecto, cambia `portfolio.admin-email` y la contraseña del administrador por una configuración segura y externa.

## Autor

**Gian Meza** · Estudiante de Ingeniería de Sistemas · Desarrollo Web con Java

## Licencia

Proyecto académico personal. Todos los derechos reservados.
