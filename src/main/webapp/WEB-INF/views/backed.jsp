<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Panel Administrador | e-Portafolio</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%-- Navegación --%>
<nav class="site-nav is-scrolled">
    <div class="container nav-inner">
        <a class="brand" href="${pageContext.request.contextPath}/#inicio">
            <span class="brand-mark">GM</span>
            <span>Backed privado</span>
        </a>
        <a class="button-secondary" href="${pageContext.request.contextPath}/logout">
            <i class="bi bi-box-arrow-right"></i> Salir
        </a>
        <a class="button-primary" href="${pageContext.request.contextPath}/">
            <i class="bi bi-eye"></i> Ver portafolio
        </a>
    </div>
</nav>

<main class="backed-page">
    <div class="container">
        <div class="section-label">Panel del administrador</div>
        <h1>Publicar evidencias.</h1>
        <p class="backed-intro">
            Los archivos se guardan en <strong>backed/evidencias</strong> y aparecen inmediatamente 
            en la página pública del portafolio.
        </p>

        <%-- Mensajes de éxito o error --%>
        <c:if test="${not empty message}">
            <div class="form-success">
                <i class="bi bi-check-circle"></i> ${message}
            </div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="form-error">
                <i class="bi bi-exclamation-circle"></i> ${error}
            </div>
        </c:if>

        <%-- Sección de carga de archivos y enlaces --%>
        <div class="upload-grid">
            <%-- Panel de subida de archivos --%>
            <section class="upload-panel">
                <h2><i class="bi bi-cloud-upload"></i> Subir trabajo</h2>
                <p class="panel-description">
Selecciona la semana y carga uno o varios archivos para publicar la evidencia.
</p>
                
                <form method="post" action="${pageContext.request.contextPath}/backed/subir" enctype="multipart/form-data" class="auth-form">
                    <label class="field">
                        Semana
                        <select name="week" required>
                            <option value="">-- Selecciona una semana --</option>
                            <c:forEach items="${weeks}" var="week">
                                <option value="${week}">
                                    Semana <fmt:formatNumber value="${week}" minIntegerDigits="2" groupingUsed="false"/>
                                </option>
                            </c:forEach>
                        </select>
                    </label>
                    
                    <label class="field">
                        Título del trabajo
                        <input type="text" name="title" placeholder="Ej: Infografía de fundamentos web" required>
                    </label>

                    <label class="field">
                        Descripción (opcional)
                        <textarea name="description" placeholder="Describe brevemente lo aprendido o entregado"></textarea>
                    </label>

                    <label class="field">
                        Archivos
                        <input 
    type="file" 
    name="files" 
    multiple
    accept=".pdf,.doc,.docx,.txt,.png,.jpg,.jpeg,.gif,.svg,.zip"
    required>
                        <small>
Puedes seleccionar varios archivos: PDF, DOC, DOCX, TXT, PNG, JPG, JPEG, SVG, GIF, ZIP
</small>
                    </label>
                    
                    <button class="button-primary" type="submit">
                        <i class="bi bi-upload"></i> Publicar archivo
                    </button>
                </form>
            </section>

            <%-- Panel para agregar enlace --%>
            <section class="upload-panel">
                <h2><i class="bi bi-link-45deg"></i> Agregar enlace</h2>
                <p class="panel-description">Publica un enlace a un repositorio, presentación o recurso externo.</p>
                
                <form method="post" action="${pageContext.request.contextPath}/backed/enlace" class="auth-form">
                    <label class="field">
                        Semana
                        <select name="week" required>
                            <option value="">-- Selecciona una semana --</option>
                            <c:forEach items="${weeks}" var="week">
                                <option value="${week}">
                                    Semana <fmt:formatNumber value="${week}" minIntegerDigits="2" groupingUsed="false"/>
                                </option>
                            </c:forEach>
                        </select>
                    </label>
                    
                    <label class="field">
                        Nombre del enlace
                        <input type="text" name="name" placeholder="Ej: Repositorio GitHub, Presentación PDF" required>
                    </label>
                    
                    <label class="field">
                        URL del enlace
                        <input type="url" name="url" placeholder="https://ejemplo.com/recurso" required>
                    </label>
                    
                    <button class="button-primary" type="submit">
                        <i class="bi bi-link-45deg"></i> Publicar enlace
                    </button>
                </form>
            </section>
        </div>

        <section class="upload-panel week-manager">
            <h2><i class="bi bi-pencil-square"></i> Actualizar trabajos</h2>
            <p class="panel-description">Edita el título, la descripción y reemplaza los archivos de una semana.</p>
            <div class="week-actions-grid">
                <c:forEach items="${weeks}" var="week">
                    <a class="mini-action" href="${pageContext.request.contextPath}/backed/actualizar/${week}">
                        <i class="bi bi-pencil"></i> Semana <fmt:formatNumber value="${week}" minIntegerDigits="2" groupingUsed="false"/>
                    </a>
                </c:forEach>
            </div>
        </section>

        <%-- Información útil --%>
        <section class="info-section" style="margin-top: 40px; padding: 20px; background: #f5f5f5; border-radius: 8px;">
            <h3><i class="bi bi-info-circle"></i> Información del sistema</h3>
            <ul>
                <li>Los archivos se guardan automáticamente en la carpeta <code>backed/evidencias/semana-XX/</code></li>
                <li>Los cambios aparecen inmediatamente en el portafolio público</li>
                <li>Puedes subir múltiples archivos por semana</li>
                <li>Se recomienda usar nombres descriptivos para los archivos</li>
                <li>El peso máximo recomendado es 50MB por archivo</li>
            </ul>
        </section>
    </div>
</main>

<script>
    // Validación del formulario de subida
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function(event) {
            const week = this.querySelector('select[name="week"]').value;
            const fileInput = this.querySelector('input[type="file"]');
            const urlInput = this.querySelector('input[type="url"]');
            
            if (!week) {
                event.preventDefault();
                alert('Por favor selecciona una semana');
                return;
            }
            
            if (fileInput && fileInput.files.length === 0) {
                event.preventDefault();
                alert('Por favor selecciona al menos un archivo');
                return;
            }
            
            if (urlInput && !urlInput.value.trim()) {
                event.preventDefault();
                alert('Por favor ingresa una URL válida');
                return;
            }
        });
    });
    
    // Mostrar nombre del archivo seleccionado
    const fileInput = document.querySelector('input[type="file"]');
    if (fileInput) {
        fileInput.addEventListener('change', function(){

    if(this.files.length > 0){

        console.log(
            "Archivos seleccionados:",
            this.files.length
        );

        for(let i=0;i<this.files.length;i++){

            console.log(
                this.files[i].name
            );

        }

    }

});
    }
</script>
</body>
</html>
