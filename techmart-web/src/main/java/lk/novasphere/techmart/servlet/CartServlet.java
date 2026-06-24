package lk.novasphere.techmart.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.novasphere.techmart.service.CartService;

import java.io.IOException;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    @Inject
    private CartService cartService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long productId = Long.parseLong(request.getParameter("productId"));
            Integer quantity = Integer.parseInt(request.getParameter("quantity"));

            cartService.addItem(productId, quantity);

            response.sendRedirect(request.getContextPath() + "/products?message=Product successfully added to cart!");

        } catch (Exception e) {
            throw new ServletException("Error adding item to cart", e);
        }
    }
}