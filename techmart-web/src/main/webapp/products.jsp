<%--
  Created by IntelliJ IDEA.
  User: Maleesha
  Date: 6/23/2026
  Time: 12:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="lk.novasphere.techmart.entity.Product" %>
<html>
<head>
    <title>TechMart - Product List</title>
</head>
<body>
<h2>TechMart - Product List (Rendered via JSP)</h2>
<table border="1" cellpadding="5">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Stock</th>
    </tr>
    <%
        List<Product> products = (List<Product>) request.getAttribute("productList");
        if (products != null) {
            for (Product p : products) {
    %>
    <tr>
        <td><%= p.getId() %></td>
        <td><%= p.getName() %></td>
        <td><%= p.getPrice() %></td>
        <td><%= p.getStock() %></td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>
