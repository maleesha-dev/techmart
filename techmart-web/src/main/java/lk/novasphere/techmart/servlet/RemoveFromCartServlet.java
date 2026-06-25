package lk.novasphere.techmart.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.novasphere.techmart.service.CartService;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/remove-from-cart")
public class RemoveFromCartServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private CartService cartService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productIdStr = request.getParameter("productId");

        if (productIdStr != null && !productIdStr.isEmpty()) {
            try {
                Long productId = Long.parseLong(productIdStr);

                cartService.getCartItems().remove(productId);

            } catch (NumberFormatException e) {

            }
        }
        response.sendRedirect(request.getContextPath() + "/checkout");
    }
}