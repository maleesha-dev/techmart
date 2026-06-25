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
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght=500;700&family=Inter:wght=400;500;600&family=JetBrains+Mono:wght=400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="topbar"></div>
<div class="page">
    <div class="page-head">
        <h2><span class="bracket">[</span>Product List<span class="bracket">]</span></h2>
        <span class="designer-tag">Design by <span class="highlight">Maleesha</span> // NovaSphere</span>
    </div>

    <% if (request.getParameter("message") != null) { %>
    <p class="flash"><%= request.getParameter("message") %></p>
    <% } %>

    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody class="product-table-body">
            <%
                List<Product> products = (List<Product>) request.getAttribute("productList");
                if (products != null) {
                    for (Product p : products) {
            %>
            <tr>
                <td class="id-cell">#<%= p.getId() %></td>
                <td class="name-cell"><%= p.getName() %></td>
                <td class="price-cell">Rs. <%= p.getPrice() %></td>
                <td>
                    <span class="stock-badge <%= p.getStock() <= 0 ? "stock-out" : "stock-ok" %>">
                        <%= p.getStock() %>
                    </span>
                </td>
                <td>
                    <form action="cart" method="POST" class="cart-form">
                        <input type="hidden" name="productId" value="<%= p.getId() %>">
                        <input type="number" name="quantity" value="1" min="1" max="<%= p.getStock() %>" class="qty-input">
                        <button type="submit" class="btn-add" <%= p.getStock() <= 0 ? "disabled" : "" %>>
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
                <td colspan="5" class="empty-row">No products found. Please refresh via ProductServlet.</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <a href="checkout" class="checkout-link">🛒 Go to Checkout / View Cart</a>
</div>
</body>
</html>