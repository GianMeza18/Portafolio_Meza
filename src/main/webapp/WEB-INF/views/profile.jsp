<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil | e-Portafolio</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%-- Navegación --%>
<nav class="site-nav is-scrolled">
    <div class="container nav-inner">
        <a class="brand" href="${pageContext.request.contextPath}/#inicio">
            <span class="brand-mark">GM</span>
            <span>e-Portafolio</span>
        </a>
        <div class="hero-actions">
            <a class="button-secondary" href="${pageContext.request.contextPath}/#evidencias">
                Evidencias
            </a>
            <a class="button-secondary" href="${pageContext.request.contextPath}/logout">
                <i class="bi bi-box-arrow-right"></i> Cerrar sesión
            </a>
        </div>
    </div>
</nav>

<main class="profile-page">
    <div class="container profile-shell">
        <section class="profile-card">
            <%-- Información del perfil del usuario --%>
            <div class="profile-header">
                <img src="${empty userProfile.photoUrl ? pageContext.request.contextPath : ''}${userProfile.photoUrl}" 
                     alt="Foto de perfil" class="profile-avatar" onerror="this.src='${pageContext.request.contextPath}/img/mezafoto.jpeg'">
                <div>
                    <div class="section-label">Perfil de usuario</div>
                    <h1>${userProfile.name}</h1>
                    <p class="profile-email">${userProfile.email}</p>
                </div>
            </div>

            <%-- Formulario para actualizar perfil --%>
            <form method="post" action="${pageContext.request.contextPath}/perfil" class="auth-form profile-form">
                <label class="field">
                    Nombre
                    <input type="text" name="name" value="${userProfile.name}" placeholder="Tu nombre">
                </label>
                
                <label class="field">
                    Correo
                    <input type="email" name="email" value="${userProfile.email}" placeholder="correo@dominio.com">
                </label>
                
                <label class="field">
                    Contraseña
                    <input type="password" name="password" placeholder="Nueva contraseña (opcional)">
                </label>
                
                <label class="field">
                    URL de foto
                    <input type="text" name="photoUrl" value="${userProfile.photoUrl}" placeholder="https://... o /img/...">
                </label>
                
                <button class="button-primary" type="submit">
                    <i class="bi bi-check-circle"></i> Guardar cambios
                </button>
            </form>
            
            <%-- Botón para volver al inicio --%>
            <a class="button-secondary" href="${pageContext.request.contextPath}/#inicio">
                <i class="bi bi-arrow-left"></i> Volver al inicio
            </a>
        </section>
    </div>
</main>

<script>
    // Script para validación y manejo del perfil
    document.querySelector('form').addEventListener('submit', function(event) {
        const name = document.querySelector('input[name="name"]').value.trim();
        const email = document.querySelector('input[name="email"]').value.trim();
        const password = document.querySelector('input[name="password"]').value.trim();
        const photoUrl = document.querySelector('input[name="photoUrl"]').value.trim();
        
        if (!name) {
            event.preventDefault();
            alert('Por favor ingresa un nombre');
            return;
        }
        
        if (!email) {
            event.preventDefault();
            alert('Por favor ingresa un correo');
            return;
        }
        
        if (!email.includes('@')) {
            event.preventDefault();
            alert('Por favor ingresa un correo válido');
            return;
        }
        
        if (password && password.length < 6) {
            event.preventDefault();
            alert('La contraseña debe tener al menos 6 caracteres');
            return;
        }
    });
</script>
</body>
</html>
