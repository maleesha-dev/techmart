<%--
  Created by IntelliJ IDEA.
  User: Maleesha
  Date: 6/23/2026
  Time: 2:19 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TechMart - Index</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght=500;700&family=Inter:wght=400;500&family=JetBrains+Mono:wght=500&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body class="body-index">
<div class="grid-glow"></div>
<div class="hero">
    <div style="margin-bottom: 20px; display: flex; justify-content: center; gap: 10px; align-items: center;">
        <span class="eyebrow" style="margin-bottom: 0;">SYSTEM ONLINE</span>
        <span class="designer-tag">By <span class="highlight">NovaSphere</span></span>
    </div>

    <h1><span class="bracket">[</span>TechMart<span class="bracket">]</span></h1>
    <p class="tagline">Parts, devices, and gear &mdash; stocked and ready.</p>

    <a href="${pageContext.request.contextPath}/products" class="cta-btn">
        📦 View Products & Gear &rarr;
    </a>
</div>
</body>
</html>