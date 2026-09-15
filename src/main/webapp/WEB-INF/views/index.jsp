<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="e-Portafolio profesional de Giancarlo Meza, estudiante de Diseño y Programación Web.">
    <title>Portafolio | Giancarlo Meza</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="https://unpkg.com/aos@2.3.4/dist/aos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%-- Loader --%>
<div class="page-loader"><div class="loader-mark"></div></div>

<%-- Navegación --%>
<nav class="site-nav">
    <div class="container nav-inner">
        <a class="brand" href="#inicio">
            <span class="brand-mark">
                <img src="${pageContext.request.contextPath}/img/DPW.png" alt="DPW Diseño y Programación Web">
            </span>
            <span>Portafolio</span>
        </a>
        <div class="nav-links">
            <a href="#sobre-mi">Sobre mí</a>
            <a href="#habilidades">Habilidades</a>
            <a href="#evidencias">Evidencias</a>
            <a href="#proyectos">Proyectos</a>
            <a href="#contacto">Contacto</a>
        </div>
        <div class="hero-actions">
            <span class="institution-mark">
                <img src="${pageContext.request.contextPath}/img/IESTP%20Cajas.png" alt="IESTP Andrés Avelino Cáceres Dorregaray">
            </span>
            <c:choose>
                <c:when test="${not empty sessionScope.user or not empty sessionScope.admin}">
                    <%-- Usuario logged in --%>
                    <a class="button-secondary" href="${pageContext.request.contextPath}/perfil">
                        <i class="bi bi-person-circle"></i> Perfil
                    </a>
                    <a class="button-secondary" href="${pageContext.request.contextPath}/logout">
                        <i class="bi bi-box-arrow-right"></i> Cerrar sesión
                    </a>
                </c:when>
                <c:otherwise>
                    <%-- No logged in --%>
                    <a class="button-secondary" href="${pageContext.request.contextPath}/login">
                        <i class="bi bi-person"></i> Acceso
                    </a>
                </c:otherwise>
            </c:choose>
            <a class="nav-cta" href="#contacto">Hablemos <i class="bi bi-arrow-up-right"></i></a>
        </div>
    </div>
</nav>

<main>
<%-- Sección Hero --%>
<section class="hero" id="inicio">
    <div class="container hero-grid">
        <div data-aos="fade-up">
            <div class="eyebrow">Disponible para aprender y crear</div>
            <h1>Portafolio de <span class="accent-text">Giancarlo Meza</span></h1>
            <p class="hero-copy">
                <strong>Giancarlo Meza</strong> · Estudiante de Diseño y Programación Web especializado 
                en Desarrollo Web con Java. Este es el registro vivo de mi evolución profesional.
            </p>
            <div class="hero-actions">
                <a class="button-primary" href="#evidencias">Ver evidencias <i class="bi bi-arrow-down-right"></i></a>
                <a class="button-secondary" href="#proyectos">Explorar proyectos</a>
                <a class="button-secondary" href="https://github.com/GianMeza18" target="_blank" rel="noopener noreferrer">
                    <i class="bi bi-github"></i> GitHub
                </a>
            </div>
        </div>
        <div class="profile-frame" data-aos="fade-left" data-aos-delay="150">
            <img src="${pageContext.request.contextPath}/img/mezafoto.jpeg" alt="Fotografía de Giancarlo Meza">
            <div class="profile-note"><b>2026</b>Proyecto de Aplicación Profesional</div>
        </div>
    </div>
</section>

<%-- Sección Sobre mí --%>
<section class="section" id="sobre-mi">
    <div class="container">
        <div class="section-heading">
            <div>
                <div class="section-label">01 / Perfil</div>
                <h2>Aprender con<br>intención.</h2>
            </div>
            <p>Un espacio para documentar decisiones, resultados y aprendizajes con la mirada puesta en crear soluciones que sí importan.</p>
        </div>
        <div class="about-grid">
            <div>
                <p class="about-lead">Estoy convirtiendo la curiosidad por la tecnología en productos web útiles, claros y mantenibles.</p>
                <p class="about-text">Actualmente estudio Diseño y Programación Web y profundizo en el ecosistema Java. 
                   Mi objetivo es unir pensamiento analítico, diseño de experiencias y buenas prácticas de programación para construir software que resuelva problemas reales.</p>
            </div>
            <div class="about-points">
                <div class="about-point">
                    <i class="bi bi-bullseye"></i>
                    <h3>Objetivo</h3>
                    <p>Crecer como desarrollador full stack.</p>
                </div>
                <div class="about-point">
                    <i class="bi bi-compass"></i>
                    <h3>Enfoque</h3>
                    <p>Calidad, orden y mejora continua.</p>
                </div>
                <div class="about-point">
                    <i class="bi bi-lightbulb"></i>
                    <h3>Intereses</h3>
                    <p>Web, datos y automatización.</p>
                </div>
                <div class="about-point">
                    <i class="bi bi-shield-check"></i>
                    <h3>Valores</h3>
                    <p>Responsabilidad y colaboración.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<%-- Sección Habilidades --%>
<section class="section" id="habilidades">
    <div class="container">
        <div class="section-heading">
            <div>
                <div class="section-label">02 / Capacidades</div>
                <h2>Herramientas<br>para avanzar.</h2>
            </div>
            <p>La base técnica que estoy fortaleciendo mediante proyectos, prácticas y evidencias de cada semana.</p>
        </div>
        <div class="skill-grid">
            <c:forEach items="${skills}" var="skill">
                <div class="skill">
                    <i class="bi bi-code-slash"></i>
                    <span>${skill}</span>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<%-- Sección Tecnologías --%>
<section class="section" id="tecnologias">
    <div class="container">
        <div class="section-heading">
            <div>
                <div class="section-label">03 / Stack</div>
                <h2>Mi caja<br>de herramientas.</h2>
            </div>
        </div>
        <div class="tech-list">
            <c:forEach items="${technologies}" var="technology">
                <div class="tech">
                    <i class="bi bi-box-arrow-up-right"></i>
                    <span>${technology}</span>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<%-- Sección Ruta de aprendizaje --%>
<section class="section" id="ruta">
    <div class="container">
        <div class="section-heading">
            <div>
                <div class="section-label">04 / Ruta 2026</div>
                <h2>Una semana a<br>la vez.</h2>
            </div>
            <p>Una línea de tiempo preparada para registrar el avance del curso durante todo el ciclo académico.</p>
        </div>
        <div class="timeline">
            <c:forEach items="${weeks}" var="week">
                <div class="timeline-item">
                    <span>Semana <fmt:formatNumber value="${week}" minIntegerDigits="2" groupingUsed="false"/></span>
                    <c:choose>
                        <c:when test="${week == 1}"><b>Fundamentos</b></c:when>
                        <c:otherwise><b>Por definir</b></c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<%-- Sección Evidencias --%>
<section class="section" id="evidencias">
    <div class="container">
        <div class="section-heading">
            <div>
                <div class="section-label">05 / Archivo principal</div>
                <h2>Evidencias que<br>cuentan la historia.</h2>
            </div>
            <p>Cada entrega captura una parte del proceso. Abre una semana para consultar sus archivos y descargarlos.</p>
        </div>
        <div class="filter-row">
            <button class="filter active" data-filter="all">Todas (${fn:length(evidences)})</button>
            <button class="filter" data-filter="ready">Completadas</button>
            <button class="filter" data-filter="pending">Pendientes</button>
        </div>
        <div class="evidence-grid">
            <c:forEach items="${evidences}" var="evidence">
                <article class="evidence-card" data-status="${evidence.status() == 'Completada' ? 'ready' : 'pending'}">
                    <div class="evidence-image">
                        <img src="${evidence.image()}" alt="Vista previa de ${evidence.title()}">
                        <span class="week-tag">${evidence.weekLabel()}</span>
                    </div>
                    <div class="evidence-body">
                        <c:choose>
                            <c:when test="${evidence.status() == 'Completada'}"><span class="status">${evidence.status()}</span></c:when>
                            <c:otherwise><span class="status pending">${evidence.status()}</span></c:otherwise>
                        </c:choose>
                        <h3>${evidence.title()}</h3>
                        <p>${evidence.description()}</p>
                        <a class="evidence-link" href="${pageContext.request.contextPath}/evidencias/${evidence.week()}">
                            Ver evidencia <i class="bi bi-arrow-up-right"></i>
                        </a>
                    </div>
                </article>
            </c:forEach>
        </div>
    </div>
</section>

<%-- Sección Proyectos --%>
<section class="section" id="proyectos">
    <div class="container">
        <div class="section-heading">
            <div>
                <div class="section-label">06 / Proyectos</div>
                <h2>De la idea<br>a la pantalla.</h2>
            </div>
            <p>Proyectos que convierten conceptos en experiencias funcionales y medibles.</p>
        </div>
        <div class="project-grid">
            <article class="project">
                <img src="https://images.unsplash.com/photo-1558655146-d09347e92766?auto=format&fit=crop&w=700&q=80" 
                     alt="Diseño de interfaz digital">
                <div class="project-content">
                    <span class="status pending">En construcción</span>
                    <h3>e-Portafolio Profesional</h3>
                    <p>Un espacio digital para organizar, presentar y reflexionar sobre el recorrido académico.</p>
                    <div class="chips">
                        <span class="chip">Spring Boot</span>
                        <span class="chip">JSP</span>
                        <span class="chip">JavaScript</span>
                    </div>
                    <a class="evidence-link" href="#inicio">Ver proyecto <i class="bi bi-arrow-up-right"></i></a>
                </div>
            </article>
            <article class="project">
                <img src="https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=700&q=80" 
                     alt="Placa electrónica y tecnología">
                <div class="project-content">
                    <span class="status pending">Próximamente</span>
                    <h3>Próximo proyecto</h3>
                    <p>Una nueva solución para explorar arquitectura, datos y desarrollo de software.</p>
                    <div class="chips">
                        <span class="chip">Java</span>
                        <span class="chip">SQL</span>
                        <span class="chip">Git</span>
                    </div>
                    <a class="evidence-link" href="#contacto">Mantener al tanto <i class="bi bi-arrow-up-right"></i></a>
                </div>
            </article>
        </div>
    </div>
</section>

<%-- Sección Galería --%>
<section class="section" id="galeria">
    <div class="container">
        <div class="section-heading">
            <div>
                <div class="section-label">07 / Bitácora visual</div>
                <h2>Fragmentos del<br>proceso.</h2>
            </div>
        </div>
        <div class="gallery">
            <img src="https://images.unsplash.com/photo-1498050108023-c5249f4df085?auto=format&fit=crop&w=900&q=80" alt="Código en pantalla">
            <img src="https://images.unsplash.com/photo-1515879218367-8466d910aaa4?auto=format&fit=crop&w=600&q=80" alt="Programación">
            <img src="https://images.unsplash.com/photo-1555066931-4365d14bab8c?auto=format&fit=crop&w=600&q=80" alt="Código fuente">
            <img src="https://images.unsplash.com/photo-1461749280684-dccba630e2f6?auto=format&fit=crop&w=600&q=80" alt="Desarrollo web">
        </div>
    </div>
</section>

<%-- Sección Contacto --%>
<section class="section" id="contacto">
    <div class="container contact-grid">
        <div class="contact-details">
            <div class="section-label">08 / Contacto</div>
            <h2>Hagamos algo<br>valioso.</h2>
            <p>¿Tienes una idea, una recomendación o quieres conocer más sobre el proceso? Este canal está abierto.</p>
            <div class="contact-item">
                <i class="bi bi-envelope"></i>
                <span>vlecmanusa18@gmail.com</span>
            </div>
            <div class="contact-item">
                <i class="bi bi-linkedin"></i>
                <span>linkedin.com/in/gian-meza-1b2a26350/</span>
            </div>
            <div class="contact-item">
                <i class="bi bi-github"></i>
                <span>https://github.com/GianMeza18</span>
            </div>
        </div>
        <form class="contact-form" action="#contacto" method="post">
            <div class="form-row">
                <label class="field">
                    Nombre
                    <input type="text" name="name" placeholder="Tu nombre" required>
                </label>
                <label class="field">
                    Correo
                    <input type="email" name="email" placeholder="tu@correo.com" required>
                </label>
            </div>
            <label class="field">
                Mensaje
                <textarea name="message" placeholder="Cuéntame un poco más..." required></textarea>
            </label>
            <button class="button-primary" type="submit">Enviar mensaje <i class="bi bi-arrow-up-right"></i></button>
        </form>
    </div>
</section>
</main>

<%-- Footer --%>
<footer>
    <div class="container footer-inner">
        <span>© 2026 Giancarlo Meza · Proyecto de Aplicación Profesional</span>
        <div class="footer-links">
            <a href="#inicio">Volver arriba</a>
            <a href="https://github.com/GianMeza18" target="_blank" rel="noopener noreferrer">GitHub</a>
            <a href="mailto:vlecmanusa18@gmail.com">Correo</a>
        </div>
    </div>
</footer>

<%-- Botón subir --%>
<button class="back-top" id="back-top" aria-label="Volver arriba"><i class="bi bi-arrow-up"></i></button>

<%-- Scripts --%>
<script src="https://unpkg.com/aos@2.3.4/dist/aos.js"></script>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
