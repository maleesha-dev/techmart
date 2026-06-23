<%--
  Created by IntelliJ IDEA.
  User: Maleesha
  Date: 6/23/2026
  Time: 12:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lk.novasphere.techmart.entity.Order" %>
<html>
<head>
    <title>TechMart - Order Status</title>
</head>
<body>
<h2>TechMart - Order Status (Rendered via JSP)</h2>
<%
    Order processedOrder = (Order) request.getAttribute("order");
    String customerName = (String) request.getAttribute("customer");

    if (processedOrder != null) {
%>
<p>Customer Name: <%= customerName %></p>
<p>Order Status: <b><%= processedOrder.getStatus() %></b></p>

<% if ("COMPLETED".equals(processedOrder.getStatus())) { %>
<p>Order ID: <%= processedOrder.getId() %></p>
<p style="color:green;">✔ Success! Background Async Notification triggered.</p>
<% } else { %>
<p style="color:red;">❌ Order Failed! Check Inventory Cache.</p>
<% } %>
<% } %>

<br>
<a href="products">Back to Products</a>
</body>
</html>
