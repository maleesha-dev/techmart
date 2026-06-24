<%--
  Created by IntelliJ IDEA.
  User: Maleesha
  Date: 6/23/2026
  Time: 12:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="lk.novasphere.techmart.entity.Product" %>
<html>
<head>
    <title>TechMart - Checkout</title>
</head>
<body>
<h2>🛒 Your Shopping Cart & Checkout</h2>

<%
    Map<Product, Integer> items = (Map<Product, Integer>) request.getAttribute("cartProducts");

    if (items == null || items.isEmpty()) {
%>
<p>Your cart is empty! <a href="products">Go back to products</a></p>
<%
} else {
%>
<table border="1" cellpadding="8" style="width: 60%; text-align: left; border-collapse: collapse;">
    <tr style="background-color: #f2f2f2;">
        <th>Product Name</th>
        <th>Price</th>
        <th>Quantity</th>
        <th>Total Price</th>
    </tr>
    <%
        double grandTotal = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            double itemTotal = product.getPrice() * quantity;
            grandTotal += itemTotal;
    %>
    <tr>
        <td><b><%= product.getName() %></b></td>
        <td>Rs. <%= product.getPrice() %></td>
        <td><%= quantity %></td>
        <td>Rs. <%= itemTotal %></td>
    </tr>
    <%
        }
    %>
    <tr style="background-color: #e6ffe6; font-weight: bold;">
        <td colspan="3" style="text-align: right;">Grand Total:</td>
        <td>Rs. <%= grandTotal %></td>
    </tr>
</table>

<br><br>
<h3>👤 Customer Details</h3>
<form action="checkout" method="POST">
    <label for="customerName">Your Name:</label>
    <input type="text" id="customerName" name="customerName" required placeholder="Enter your name" style="padding: 5px; width: 250px;">
    <br><br>
    <button type="submit" style="background-color: green; color: white; padding: 10px 20px; font-weight: bold; border: none; cursor: pointer;">
        Place Order
    </button>
</form>
<%
    }
%>

<br><br>
<a href="products">⬅️ Continue Shopping</a>
</body>
</html>
