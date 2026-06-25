package lk.novasphere.techmart.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.novasphere.techmart.entity.Order;
import lk.novasphere.techmart.entity.Product;
import lk.novasphere.techmart.messaging.OrderProducer;

import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    @PersistenceContext
    private EntityManager em;

    @EJB
    private InventoryCache inventoryCache;

    @EJB
    private OrderProducer orderProducer;

    @EJB
    private TechMartAnalyticsService analyticsService;

    public Order checkout(String customerName, Map<Long, Integer> cartItems) {

        long startTime = System.currentTimeMillis();

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setStatus("PENDING");

        double totalAmount = 0.0;
        boolean transactionSuccess = true;

        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long productId = entry.getKey();
            Integer orderQty = entry.getValue();

            boolean deducted = inventoryCache.deductStock(productId, orderQty);
            if (!deducted) {
                transactionSuccess = false;
                LOGGER.warning("[STOCK OUT] : Insufficient stock in Cache for Product ID: " + productId);
                break;
            }
        }

        if (transactionSuccess) {
            for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
                Long productId = entry.getKey();
                Integer orderQty = entry.getValue();

                Product product = em.find(Product.class, productId);
                if (product != null) {
                    totalAmount += product.getPrice() * orderQty;

                    int newStock = product.getStock() - orderQty;
                    product.setStock(newStock);
                    em.merge(product);
                }
            }

            order.setTotalAmount(totalAmount);
            order.setStatus("COMPLETED");

            em.persist(order);
            em.flush();

            try {
                orderProducer.sendOrderMessage(order.getId(), customerName, totalAmount);
                LOGGER.info("[JMS SUCCESS] : Order message sent to ActiveMQ Queue.");
            } catch (Exception e) {
                LOGGER.severe("[JMS ERROR] : Producer Call Failed: " + e.getMessage());
            }

            try {
                analyticsService.processOrderAnalytics(order.getId());
            } catch (Exception e) {
                LOGGER.warning("[ASYNC WARN] : Analytics execution skipped: " + e.getMessage());
            }

        } else {
            order.setStatus("FAILED");
            LOGGER.severe("[ORDER FAILED] : Checkout failed due to insufficient stock for " + customerName);
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        LOGGER.info("[METRICS] : checkout() execution time for client '" + customerName + "': " + executionTime + " ms");

        return order;
    }
}