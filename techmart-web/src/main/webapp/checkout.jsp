<%--
  Created by IntelliJ IDEA.
  User: Maleesha
  Date: 6/23/2026
  Time: 12:56 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="lk.novasphere.techmart.entity.Product" %>
<html>
<head>
    <title>TechMart - Checkout</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;700&family=Inter:wght@400;500;600&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="topbar"></div>
<div class="page page-checkout">
    <h2><span class="bracket">[</span>Your Cart &amp; Checkout<span class="bracket">]</span></h2>

    <%
        Map<Product, Integer> items = (Map<Product, Integer>) request.getAttribute("cartProducts");

        if (items == null || items.isEmpty()) {
    %>
    <div class="empty-state">
        <p>Your cart is empty.</p>
        <a href="products">Go back to products &rarr;</a>
    </div>
    <%
    } else {
    %>
    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>Product Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Total Price</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <%
                double grandTotal = 0.0;
                for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                    Product product = entry.getKey();
                    Integer quantity = entry.getValue();
                    double itemTotal = product.getPrice() * quantity;
                    grandTotal += itemTotal;
            %>
            <tr>
                <td class="name-cell"><%= product.getName() %></td>
                <td class="num-cell">Rs. <%= product.getPrice() %></td>
                <td class="num-cell"><%= quantity %></td>
                <td class="num-cell">Rs. <%= itemTotal %></td>
                <td>
                    <form action="${pageContext.request.contextPath}/remove-from-cart" method="POST">
                        <input type="hidden" name="productId" value="<%= product.getId() %>">
                        <button type="submit" class="btn-remove">&times; Remove</button>
                    </form>
                </td>
            </tr>
            <%
                }
            %>
            <tr class="grand-row">
                <td colspan="3" class="grand-label">Grand Total</td>
                <td class="num-cell">Rs. <%= grandTotal %></td>
                <td></td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="customer-block">
        <h3>Customer Details</h3>
        <form action="checkout" method="POST">
            <div class="field-group">
                <label for="customerName">Your Name</label>
                <input type="text" id="customerName" name="customerName" required placeholder="Enter your name">
            </div>
            <button type="submit" class="btn-place">Place Order</button>
        </form>
    </div>
    <%
        }
    %>

    <a href="products" class="continue-link">&larr; Continue Shopping</a>
</div>
</body>
</html>