<%--
  Created by IntelliJ IDEA.
  User: Maleesha
  Date: 6/23/2026
  Time: 12:55 PM
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

<% if (request.getParameter("message") != null) { %>
<p style="color: green; font-weight: bold;"><%= request.getParameter("message") %>
</p>
<% } %>

<table border="1" cellpadding="5">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Stock</th>
        <th>Action</th>
    </tr>
    <%
        List<Product> products = (List<Product>) request.getAttribute("productList");
        if (products != null) {
            for (Product p : products) {
    %>
    <tr>
        <td><%= p.getId() %>
        </td>
        <td><%= p.getName() %>
        </td>
        <td><%= p.getPrice() %>
        </td>
        <td><%= p.getStock() %>
        </td>
        <td>
            <form action="cart" method="POST" style="margin: 0;">
                <input type="hidden" name="productId" value="<%= p.getId() %>">
                <input type="number" name="quantity" value="1" min="1" max="<%= p.getStock() %>" style="width: 50px;">
                <button type="submit" <%= p.getStock() <= 0 ? "disabled" : "" %>>
                    <%= p.getStock() <= 0 ? "Out of Stock" : "Add to Cart" %>
                </button>
            </form>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="5">No products found. Please refresh via ProductServlet.</td>
    </tr>
    <% } %>
</table>

<br>
<a href="checkout" style="font-weight: bold; font-size: 16px; color: blue;">🛒 Go to Checkout / View Cart</a>
</body>
</html>