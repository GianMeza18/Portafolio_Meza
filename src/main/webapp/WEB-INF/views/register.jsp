<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registro | e-Portafolio</title>
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
        
        <div class="section-label">Registro</div>
        <h1>Crear cuenta</h1>
        <p class="auth-intro">
            El portafolio es público para docentes y visitantes. 
            El panel de publicación está reservado al administrador.
        </p>
        
        <%-- Mostrar mensaje de éxito si existe --%>
        <c:if test="${not empty message}">
            <div class="form-success">
                <i class="bi bi-check-circle"></i> ${message}
            </div>
        </c:if>
        
        <%-- Mostrar mensaje de error si existe --%>
        <c:if test="${not empty error}">
            <div class="form-error">
                <i class="bi bi-exclamation-circle"></i> ${error}
            </div>
        </c:if>
        
        <%-- Formulario de registro --%>
        <form method="post" action="${pageContext.request.contextPath}/registro" class="auth-form">
            <label class="field">
                Correo
                <input type="email" name="email" placeholder="correo@dominio.com" required>
            </label>
            
            <label class="field">
                Contraseña
                <input type="password" name="password" placeholder="Mínimo 6 caracteres" minlength="6" required>
            </label>
            
            <button class="button-primary" type="submit">Registrarse <i class="bi bi-arrow-right"></i></button>
        </form>
        
        <a class="button-secondary auth-back" href="${pageContext.request.contextPath}/login">
            Ir al login
        </a>
    </div>
</main>

<script>
    // Script para validación de registro
    document.querySelector('form').addEventListener('submit', function(event) {
        const email = document.querySelector('input[name="email"]').value.trim();
        const password = document.querySelector('input[name="password"]').value.trim();
        
        if (!email) {
            event.preventDefault();
            alert('Por favor ingresa un correo electrónico');
            return;
        }
        
        if (password.length < 6) {
            event.preventDefault();
            alert('La contraseña debe tener al menos 6 caracteres');
            return;
        }
        
        if (!email.includes('@')) {
            event.preventDefault();
            alert('Por favor ingresa un correo válido');
            return;
        }
    });
</script>
</body>
</html>
