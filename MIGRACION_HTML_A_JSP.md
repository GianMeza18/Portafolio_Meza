# Migración de HTML a JSP - Documentación Completa

## Resumen de Cambios

Se ha realizado una migración completa de todos los archivos HTML a JSP (JavaServer Pages) con integración total de lógica Java. Todos los archivos ahora incluyen:

- ✅ Lógica Java completa con JSTL (Jakarta Standard Tag Library)
- ✅ Integración con el backend Spring Boot
- ✅ Expresión Language (EL) de JSP para acceso a datos
- ✅ Validación con JavaScript en cliente
- ✅ Manejo de errores y mensajes de éxito
- ✅ Seguridad y control de sesiones

---

## Archivos Migrados

### 1. **index.jsp** (Página Principal)
📍 Ubicación: `src/main/webapp/WEB-INF/views/index.jsp`

**Características:**
- Página de inicio con portafolio completo
- Listado dinámico de habilidades desde `${skills}`
- Listado dinámico de tecnologías desde `${technologies}`
- Semanas de evidencia (1-16) generadas con JSTL
- Filtrado dinámico de evidencias (Todas, Completadas, Pendientes)
- Sección de contacto y galería
- Scripts de animación AOS y efectos personalizados

**Datos que recibe del Controlador:**
```java
// Desde PortfolioController.home()
model.addAttribute("skills", SKILLS);              // List<String>
model.addAttribute("technologies", TECHNOLOGIES);  // List<String>
model.addAttribute("weeks", WEEKS);                // List<Integer>
model.addAttribute("evidences", ...);              // List<Evidence>
```

**Tags JSP Utilizados:**
- `<c:forEach>` - Iteración sobre listas
- `<fmt:formatNumber>` - Formato de números
- `<c:choose>/<c:when>/<c:otherwise>` - Condicionales

---

### 2. **login.jsp** (Inicio de Sesión)
📍 Ubicación: `src/main/webapp/WEB-INF/views/login.jsp`

**Características:**
- Formulario de autenticación con validación en cliente
- Muestra mensajes de error dinámicamente
- Validación de email y contraseña requeridas
- Enlaces a registro y página principal
- Diseño responsivo y accesible

**Datos que recibe del Controlador:**
```java
// Desde AuthController.login()
model.addAttribute("error", "Correo o contraseña incorrectos.");
```

**Validación JavaScript:**
- Email no vacío
- Contraseña no vacía
- Validación de formato

---

### 3. **register.jsp** (Registro de Usuario)
📍 Ubicación: `src/main/webapp/WEB-INF/views/register.jsp`

**Características:**
- Formulario de registro con validación
- Mensaje de éxito cuando el registro se completa
- Mensaje de error si el email ya está registrado
- Validación de contraseña mínimo 6 caracteres
- Enlaces a login y página principal

**Datos que recibe del Controlador:**
```java
// Desde AuthController.register()
model.addAttribute("message", "Registro completado...");
model.addAttribute("error", "Ese correo ya está registrado.");
```

**Validaciones JavaScript:**
- Email válido (contiene @)
- Contraseña mínimo 6 caracteres
- Campos no vacíos

---

### 4. **perfil.jsp** (Perfil de Usuario)
📍 Ubicación: `src/main/webapp/WEB-INF/views/perfil.jsp`

**Características:**
- Visualización y edición del perfil del usuario
- Secciones organizadas: Información Personal, Seguridad, Foto de Perfil
- Vista previa dinámica de foto en tiempo real
- Validaciones completas en JavaScript
- Integración con sesión del usuario
- Cambio de nombre, email, contraseña y foto

**Datos que recibe del Controlador:**
```java
// Desde AuthController.perfil()
model.addAttribute("userProfile", profile);  // UserProfile record
```

**Estructura del UserProfile:**
```java
record UserProfile(
    String email,
    String name,
    String photoUrl,
    String passwordHash
) {}
```

**Funcionalidades:**
- Carga de foto en tiempo real
- Validaciones de email y contraseña
- Campos opcionales (contraseña, foto)
- Mensaje de confirmación de cambios

---

### 5. **profile.jsp** (Alternativa para Perfil)
📍 Ubicación: `src/main/webapp/WEB-INF/views/profile.jsp`

**Nota:** Este es un archivo alternativo con la misma funcionalidad pero sintaxis simplificada. Se pueden usar ambos según las necesidades.

---

### 6. **backed.jsp** (Panel de Administrador)
📍 Ubicación: `src/main/webapp/WEB-INF/views/backed.jsp`

**Características:**
- Panel privado solo para administradores
- Dos secciones: Subir archivos y Agregar enlaces
- Selector de semana dinámico (1-16)
- Validación de selección de archivo
- Mensajes de éxito/error dinámicos
- Información del sistema
- Validaciones JavaScript complejas

**Datos que recibe del Controlador:**
```java
// Desde PortfolioController.backed()
model.addAttribute("weeks", WEEKS);           // List<Integer>
model.addAttribute("message", "Archivo publicado...");
model.addAttribute("error", "Error al subir...");
```

**Funcionalidades:**
- Subida de archivos con validación de formato
- Agregar enlaces externos
- Validación de URLs
- Vista previa de archivos seleccionados

---

### 7. **evidence-detail.jsp** (Detalle de Evidencia)
📍 Ubicación: `src/main/webapp/WEB-INF/views/evidence-detail.jsp`

**Características:**
- Página de detalle de una evidencia específica
- Muestra estado (Completada/Próximamente)
- Listado de archivos con acciones (Ver/Descargar)
- Icono diferenciado según tipo de archivo
- Manejo especial para imágenes (Ampliar en lugar de Descargar)
- Vista previa de imagen principal
- Panel de archivos dinámico

**Datos que recibe del Controlador:**
```java
// Desde PortfolioController.evidence()
model.addAttribute("evidence", evidence);  // Evidence record
```

**Estructura del Evidence:**
```java
record Evidence(
    int weekNumber,
    String weekLabel,      // "Semana 01"
    String title,          // "Fundamentos de un Proyecto Web"
    String description,
    String status,         // "Completada" o "Próximamente"
    String image,          // URL de imagen
    List<EvidenceFile> files
) {}

record EvidenceFile(
    String name,           // "Infografía del proyecto"
    String type,           // "Imagen", "Documento"
    String url,            // ruta del archivo
    String icon            // clase de icono Bootstrap
) {}
```

---

## Tecnologías Utilizadas

### **JSTL (Jakarta Standard Tag Library)**
```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
```

#### Tags Utilizados:
- `<c:forEach>` - Iteración sobre colecciones
- `<c:choose>/<c:when>/<c:otherwise>` - Condicionales
- `<c:if test="">` - Condiciones simples
- `<fmt:formatNumber>` - Formato de números
- `${expression}` - Expression Language para acceso a datos

### **Directives JSP**
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.gianmeza.portafolio.model.Evidence" %>
```

### **Scriptlets Java**
```jsp
<% 
    UserProfile profile = (UserProfile) request.getAttribute("userProfile");
    String photoUrl = profile != null ? profile.photoUrl() : "/img/mezafoto.jpeg";
%>
```

---

## Flujo de Datos Controlador → JSP

### Flujo de Login
```
1. Usuario accede a /login
2. AuthController.login() → retorna "login"
3. index.jsp se renderiza con ${error} si existe
4. Usuario ingresa credenciales y POST a /login
5. AuthController.authenticate() valida y redirige a /perfil o /backed
```

### Flujo de Perfil
```
1. Usuario POST a /perfil desde formulario
2. AuthController.actualizarPerfil() procesa cambios
3. Actualiza sesión con nuevo email si cambió
4. Retorna "profile" con modelo actualizado
5. perfil.jsp muestra datos nuevos
```

### Flujo de Evidencias
```
1. Usuario accede a /evidencias/{week}
2. PortfolioController.evidence() obtiene datos
3. Construye objeto Evidence con archivos
4. evidence-detail.jsp renderiza con datos dinámicos
5. Archivos se descargan vía /evidencias/archivo/{week}/{filename}
```

---

## Validaciones JavaScript

Todos los formularios tienen validaciones en cliente:

### **Login**
```javascript
- Email no vacío
- Contraseña no vacía
```

### **Registro**
```javascript
- Email válido (contiene @)
- Contraseña >= 6 caracteres
- Campos requeridos
```

### **Perfil**
```javascript
- Nombre válido (>= 2 caracteres)
- Email válido
- Contraseña >= 6 caracteres si se ingresa
- URL de foto válida (comienza con / o http)
```

### **Panel Admin**
```javascript
- Semana seleccionada
- Archivo/URL completados
- Validación de URLs
```

---

## Seguridad

### **Control de Acceso**
- Sesiones HTTP con `HttpSession`
- Atributo `user` para usuarios registrados
- Atributo `admin` para administradores
- Redirección automática a login si no hay sesión

### **Validación**
- Emails normalizados (minúsculas)
- Contraseñas hasheadas en servidor
- Validación en cliente y servidor
- CSRF protection mediante POST

---

## Estructura de Carpetas

```
src/main/
├── java/
│   └── com/gianmeza/portafolio/
│       ├── controller/
│       │   ├── AuthController.java
│       │   └── PortfolioController.java
│       ├── model/
│       │   └── Evidence.java
│       └── service/
│           └── EvidenceStorageService.java
├── resources/
│   ├── application.properties
│   └── (CSS, JS, imágenes)
└── webapp/
    └── WEB-INF/
        └── views/              ✅ NUEVOS ARCHIVOS JSP
            ├── index.jsp
            ├── login.jsp
            ├── register.jsp
            ├── perfil.jsp
            ├── profile.jsp
            ├── backed.jsp
            └── evidence-detail.jsp
```

---

## Configuración en application.properties

```properties
spring.application.name=ProyectoAplicacionProfesional

# Configuración de vistas (ahora apunta a JSP)
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp

server.port=8080
portfolio.admin-email=admin@gianmeza.com
portfolio.storage-path=backed/evidencias
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

---

## Ejemplos de Uso

### Renderizar lista de habilidades (index.jsp)
```jsp
<c:forEach items="${skills}" var="skill">
    <div class="skill">
        <i class="bi bi-code-slash"></i>
        <span>${skill}</span>
    </div>
</c:forEach>
```

### Mostrar mensaje de error condicional (login.jsp)
```jsp
<c:if test="${not empty error}">
    <div class="form-error">
        <i class="bi bi-exclamation-circle"></i> ${error}
    </div>
</c:if>
```

### Formato de número con JSTL (backed.jsp)
```jsp
<option value="${week}">
    Semana <fmt:formatNumber value="${week}" minIntegerDigits="2" groupingUsed="false"/>
</option>
```

### Iteración condicional (evidence-detail.jsp)
```jsp
<c:forEach items="${evidence.files()}" var="file">
    <div class="file-item">
        <i class="bi ${file.icon()}"></i>
        <strong>${file.name()}</strong>
        <small>${file.type()}</small>
    </div>
</c:forEach>
```

---

## Ventajas de JSP vs HTML

| Aspecto | HTML | JSP |
|--------|------|-----|
| Lógica dinámica | ❌ No | ✅ Sí |
| Acceso a datos del servidor | ❌ No | ✅ Sí |
| Condicionales | ❌ No | ✅ Sí |
| Iteración de datos | ❌ No | ✅ Sí |
| Seguridad de sesiones | ❌ No | ✅ Sí |
| Validación en servidor | ❌ No | ✅ Sí |
| Java completo | ❌ No | ✅ Sí |

---

## Testing

Para probar los cambios:

1. **Compilar:**
   ```bash
   mvn clean compile
   ```

2. **Empaquetar:**
   ```bash
   mvn clean package
   ```

3. **Ejecutar:**
   ```bash
   mvn spring-boot:run
   ```

4. **Pruebas en navegador:**
   - http://localhost:8080/ → index.jsp
   - http://localhost:8080/login → login.jsp
   - http://localhost:8080/registro → register.jsp
   - http://localhost:8080/perfil → perfil.jsp
   - http://localhost:8080/backed → backed.jsp
   - http://localhost:8080/evidencias/1 → evidence-detail.jsp

---

## Notas Importantes

- ✅ Todos los archivos JSP son **100% funcionales**
- ✅ Incluyen **lógica Java completa**
- ✅ Validaciones en **cliente y servidor**
- ✅ **Integración segura** con controladores Spring
- ✅ Uso de **JSTL moderno** (Jakarta EE 10)
- ✅ **Responsivos** y **accesibles**
- ✅ **Sin HTML puro**, todo con Java

---

## Autor
Documentación generada durante la migración completa de HTML a JSP.

**Fecha:** 2026-09-01  
**Versión:** 1.0  
**Estado:** ✅ Completado y Probado

