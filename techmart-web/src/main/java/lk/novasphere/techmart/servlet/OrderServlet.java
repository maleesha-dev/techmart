package lk.novasphere.techmart.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.novasphere.techmart.entity.Order;
import lk.novasphere.techmart.service.CartService;
import lk.novasphere.techmart.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;

@WebServlet("/checkout")
public class OrderServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @EJB
    private OrderService orderService;

    @EJB
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String customerName = request.getParameter("customer");
        if (customerName == null || customerName.isEmpty()) {
            customerName = "Guest Customer";
        }

        cartService.clearCart();
        cartService.addItem(1L, 2);

        Order processedOrder = orderService.checkout(customerName, cartService.getCartItems());

        request.setAttribute("order", processedOrder);
        request.setAttribute("customer", customerName);

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }
}