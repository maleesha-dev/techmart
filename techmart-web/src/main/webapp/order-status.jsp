<%--
  Created by IntelliJ IDEA.
  User: Maleesha
  Date: 6/24/2026
  Time: 11:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lk.novasphere.techmart.entity.Order" %>
<html>
<head>
    <title>TechMart - Order Status</title>
</head>
<body>
<%
    Order order = (Order) request.getAttribute("processedOrder");
    String customerName = (String) request.getAttribute("customerName");

    if (order != null && "COMPLETED".equals(order.getStatus())) {
%>
<h2 style="color: green;">🎉 Order Placed Successfully!</h2>
<p>Thank you <b><%= customerName %> </b></p>
<p>Total Amount: <b>Rs. <%= order.getTotalAmount() %></b></p>
<%
} else {
%>
<h2 style="color: red;">❌ Order Failed!</h2>
<p>Reason: Out of stock for one or more items in your cart.</p>
<%
    }
%>

<br>
<a href="products">Return to Home / Products</a>
</body>
</html>