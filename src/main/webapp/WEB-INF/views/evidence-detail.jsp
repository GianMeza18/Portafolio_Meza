<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.gianmeza.portafolio.model.Evidence" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${evidence.weekLabel()} | e-Portafolio</title>
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
        <a class="button-secondary" href="${pageContext.request.contextPath}/#evidencias">
            <i class="bi bi-arrow-left"></i> Volver a evidencias
        </a>
        <c:if test="${sessionScope.admin == true}">
            <a class="button-secondary" href="${pageContext.request.contextPath}/backed/actualizar/${evidence.week()}">
                <i class="bi bi-pencil-square"></i> Actualizar trabajos
            </a>
        </c:if>
    </div>
</nav>

<main class="evidence-detail">
<%
    Evidence evidence = (Evidence) request.getAttribute("evidence");
%>
    <div class="container">
        <%-- Encabezado de la evidencia --%>
        <div class="section-label">${evidence.weekLabel()}</div>
        
        <div class="detail-heading">
            <div>
                <%-- Estado de la evidencia --%>
                <c:choose>
                    <c:when test="${evidence.status() == 'Completada'}">
                        <span class="status">${evidence.status()}</span>
                    </c:when>
                    <c:otherwise>
                        <span class="status pending">${evidence.status()}</span>
                    </c:otherwise>
                </c:choose>
                
                <h1>${evidence.title()}</h1>
                <p>${evidence.description()}</p>
            </div>
            
            <%-- Botón para ver el primer archivo si existe --%>

<c:if test="${not empty evidence.files()}">
    <%
        if (!evidence.files().isEmpty()) {

            Evidence.EvidenceFile docFile = null;

            for (Evidence.EvidenceFile file : evidence.files()) {
                if (file.type().equals("PDF")) {
                    docFile = file;
                    break;
                }
            }

            if (docFile == null) {
                for (Evidence.EvidenceFile file : evidence.files()) {
                    if (!file.type().equals("Imagen")) {
                        docFile = file;
                        break;
                    }
                }
            }

            if (docFile == null) {
                docFile = evidence.files().get(0);
            }

            String viewUrl = docFile.url().replace(
                    "/evidencias/archivo/",
                    "/evidencias/ver/");
    %>

    <a class="button-primary"
       href="<%= viewUrl %>"
       target="_blank"
       rel="noopener">
        <i class="bi bi-eye"></i>
        Ver archivo
    </a>

    <%
        }
    %>
</c:if>
        </div>

        <%-- Contenido principal --%>
        <div class="detail-layout">
            <%-- Descripción --%>
            <section class="evidence-description">
                <h2>Descripción</h2>
                <p>${evidence.description()}</p>
            </section>

            <%-- Vista previa de imagen --%>
            <section class="evidence-image">
                <h2>Vista previa</h2>
                <div class="evidence-preview">
                    <img src="${evidence.image()}" alt="Vista previa de la evidencia">
                </div>
            </section>

            <%-- Documento --%>
            <section class="evidence-document">
                <h2>Documento</h2>
                <%
                            if (evidence == null || evidence.files().isEmpty()) {
                %>
                        <p class="empty-state">
                            <i class="bi bi-info-circle"></i>
                            El documento de esta semana se publicará próximamente.
                        </p>
                <%
                    } else {
                        for (Evidence.EvidenceFile file : evidence.files()) {
                            String viewUrl = file.external() ? file.url() : file.url().replace("/evidencias/archivo/", "/evidencias/ver/");
                            String downloadUrl = file.url();
                            request.setAttribute("currentFile", file);
                            request.setAttribute("viewUrl", viewUrl);
                            request.setAttribute("downloadUrl", downloadUrl);
                %>
                            <div class="file-item">
                                <i class="bi ${currentFile.icon()}"></i>
                                <div>
                                    <strong>${currentFile.name()}</strong>
                                    <small>${currentFile.type()}</small>
                                </div>
                                <div class="file-actions">
                                    <a class="mini-action" href="${viewUrl}" target="_blank" rel="noopener" title="Ver archivo">
                                        <i class="bi bi-eye"></i> Ver
                                    </a>
                                    <a class="mini-action" href="${downloadUrl}" rel="noopener" title="Descargar archivo">
                                        <i class="bi bi-download"></i> Descargar
                                    </a>
                                </div>
                            </div>
                <%
                        }
                    }
                %>
            </section>
        </div>
    </div>
</main>

<%-- Footer --%>
<footer>
    <div class="container footer-inner">
        <span>© 2026 Giancarlo Meza · Proyecto de Aplicación Profesional</span>
        <a href="${pageContext.request.contextPath}/#inicio">Volver al inicio</a>
    </div>
</footer>

<script>
    // Script para manejo de archivos
    document.querySelectorAll('.file-item').forEach(item => {
        item.addEventListener('mouseenter', function() {
            this.style.backgroundColor = '#f5f5f5';
        });
        
        item.addEventListener('mouseleave', function() {
            this.style.backgroundColor = 'transparent';
        });
    });
    
    // Log para depuración
    console.log('Evidencia cargada:', {
        title: '${evidence.title()}',
        week: '${evidence.weekLabel()}',
        status: '${evidence.status()}',
        filesCount: <%= ((Evidence) request.getAttribute("evidence")).files().size() %>
    });
</script>
</body>
</html>
