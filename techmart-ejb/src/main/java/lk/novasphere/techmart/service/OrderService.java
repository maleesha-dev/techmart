package lk.novasphere.techmart.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.novasphere.techmart.entity.Order;
import lk.novasphere.techmart.entity.Product;
import lk.novasphere.techmart.messaging.OrderProducer;

import java.util.Map;

@Stateless
public class OrderService {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private InventoryCache inventoryCache;

    @EJB
    private OrderProducer orderProducer;

    public Order checkout(String customerName, Map<Long, Integer> cartItems) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setStatus("PENDING");

        double totalAmount = 0.0;
        boolean isStockAvailable = true;

        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long productId = entry.getKey();
            Integer orderQty = entry.getValue();

            Product product = em.find(Product.class, productId);
            if (product == null || product.getStock() < orderQty) {
                isStockAvailable = false;
                break;
            }
        }

        if (isStockAvailable) {
            for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
                Long productId = entry.getKey();
                Integer orderQty = entry.getValue();

                Product product = em.find(Product.class, productId);
                totalAmount += product.getPrice() * orderQty;

                int newStock = product.getStock() - orderQty;
                product.setStock(newStock);
                em.merge(product);

                inventoryCache.updateStock(productId, newStock);
            }

            order.setTotalAmount(totalAmount);
            order.setStatus("COMPLETED");

            em.persist(order);
            em.flush();


            try {

                orderProducer.sendOrderMessage(order.getId(), customerName, totalAmount);
                System.out.println("Order success message delegated to OrderProducer.");
            } catch (Exception e) {
                System.err.println("Producer Call Error: " + e.getMessage());
            }

        } else {
            order.setStatus("FAILED");
        }

        return order;
    }
}