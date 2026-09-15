<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Acceso | e-Portafolio</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<main class="auth-page">
    <div class="auth-box">
        <a class="brand" href="${pageContext.request.contextPath}/#inicio">
            <span class="brand-mark">GM</span>
            <span>e-Portafolio</span>
        </a>
        
        <div class="section-label">Acceso</div>
        <h1>Iniciar sesión</h1>
        <p class="auth-intro">Accede al portafolio o al panel privado según tu cuenta.</p>
        
        <%-- Mostrar mensaje de error si existe --%>
        <c:if test="${not empty error}">
            <div class="form-error">
                <i class="bi bi-exclamation-circle"></i> ${error}
            </div>
        </c:if>
        
        <%-- Formulario de login --%>
        <form method="post" action="${pageContext.request.contextPath}/login" class="auth-form">
            <label class="field">
                Correo
                <input type="email" name="email" placeholder="correo@dominio.com" required>
            </label>
            
            <label class="field">
                Contraseña
                <input type="password" name="password" required>
            </label>
            
            <button class="button-primary" type="submit">Ingresar <i class="bi bi-arrow-right"></i></button>
        </form>
        
        <a class="button-secondary auth-back" href="${pageContext.request.contextPath}/registro">
            Registrarse
        </a>
        <a class="button-secondary auth-back" href="${pageContext.request.contextPath}/#inicio">
            Volver al portafolio
        </a>
    </div>
</main>

<script>
    // Script para validación de formulario en JSP
    document.querySelector('form').addEventListener('submit', function(event) {
        const email = document.querySelector('input[name="email"]').value.trim();
        const password = document.querySelector('input[name="password"]').value.trim();
        
        if (!email || !password) {
            event.preventDefault();
            alert('Por favor completa todos los campos');
        }
    });
</script>
</body>
</html>
