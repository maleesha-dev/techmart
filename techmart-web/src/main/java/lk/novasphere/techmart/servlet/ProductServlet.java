package lk.novasphere.techmart.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.novasphere.techmart.entity.Product;
import lk.novasphere.techmart.service.ProductService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<Product> products = productService.getAllProducts();

        request.setAttribute("productList", products);

        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        Double price = Double.parseDouble(request.getParameter("price"));
        Integer stock = Integer.parseInt(request.getParameter("stock"));

        Product newProduct = new Product(name, price, stock);
        productService.createProduct(newProduct);

        response.sendRedirect(request.getContextPath() + "/products");
    }
}