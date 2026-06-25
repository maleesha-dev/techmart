<%--
  Created by IntelliJ IDEA.
  User: Maleesha
  Date: 6/24/2026
  Time: 11:34 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lk.novasphere.techmart.entity.Order" %>
<html>
<head>
    <title>TechMart - Order Status</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;700&family=Inter:wght@400;500;600&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body class="body-status">
<div class="topbar"></div>
<%
    Order order = (Order) request.getAttribute("processedOrder");
    String customerName = (String) request.getAttribute("customerName");

    if (order != null && "COMPLETED".equals(order.getStatus())) {
%>
<div class="card">
    <div class="status-icon ok">✅</div>
    <h2 class="ok">Order Placed Successfully!</h2>
    <p>Thank you, <b style="color: var(--text);"><%= customerName %></b></p>
    <div class="amount">Rs. <%= order.getTotalAmount() %></div>
    <a href="products" class="home-link">Return to Home / Products</a>
</div>
<%
} else {
%>
<div class="card">
    <div class="status-icon fail">❌</div>
    <h2 class="fail">Order Failed</h2>
    <p>Reason: Out of stock for one or more items in your cart.</p>
    <a href="products" class="home-link">Return to Home / Products</a>
</div>
<%
    }
%>
</body>
</html>