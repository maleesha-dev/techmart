package lk.novasphere.techmart.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.novasphere.techmart.entity.Order;
import lk.novasphere.techmart.entity.Product;
import lk.novasphere.techmart.service.CartService;
import lk.novasphere.techmart.service.OrderService;
import lk.novasphere.techmart.service.ProductService;

import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/checkout")
public class OrderServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @EJB
    private OrderService orderService;

    @EJB
    private ProductService productService;

    @jakarta.inject.Inject
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Map<Product, Integer> productsInCart = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : cartService.getCartItems().entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productService.getProductById(productId);
            if (product != null) {
                productsInCart.put(product, quantity);
            }
        }

        request.setAttribute("cartProducts", productsInCart);
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String customerName = request.getParameter("customerName");
        if (customerName == null || customerName.isEmpty()) {
            customerName = "Guest Customer";
        }

        if (cartService.getCartItems().isEmpty()) {
            response.sendRedirect("products");
            return;
        }

        Order processedOrder = orderService.checkout(customerName, cartService.getCartItems());

        request.setAttribute("processedOrder", processedOrder);
        request.setAttribute("customerName", customerName);

        if ("COMPLETED".equals(processedOrder.getStatus())) {
            cartService.clearCart();
        }

        request.getRequestDispatcher("/order-status.jsp").forward(request, response);
    }
}