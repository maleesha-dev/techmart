package lk.novasphere.techmart.service;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.novasphere.techmart.entity.Order;
import lk.novasphere.techmart.entity.Notification;
import java.util.Date;
import java.util.concurrent.Future;
import jakarta.ejb.AsyncResult;
import java.util.logging.Logger;

@Stateless
public class OrderService {

    private static final Logger LOGGER =
            Logger.getLogger(OrderService.class.getName());

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Inject
    private InventoryCache inventoryCache;

    public Order checkout(String customerName, java.util.Map<Long, Integer> cartItems) {

        Double totalAmount = 0.0;
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setStatus("PENDING");

        for (java.util.Map.Entry<Long, Integer> item : cartItems.entrySet()) {
            boolean success = inventoryCache.deductStock(item.getKey(), item.getValue());
            if (!success) {
                order.setStatus("FAILED");
                LOGGER.warning("Order Failed: Out of stock for Product ID: " + item.getKey());
                return order;
            }
            totalAmount += (item.getValue() * 100.0);
        }

        order.setTotalAmount(totalAmount);
        order.setStatus("COMPLETED");

        em.persist(order);
        em.flush();

        LOGGER.info("Order Successfully Placed! Order ID: " + order.getId());

        sendOrderNotification(order.getId());

        return order;
    }

    @Asynchronous
    public Future<String> sendOrderNotification(Long orderId) {
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(3000);

            Notification notification = new Notification(orderId, "Your TechMart Order #" + orderId + " has been processed successfully!");
            em.persist(notification);

            long endTime = System.currentTimeMillis();
            LOGGER.info("Notification sent for Order #" + orderId + " (Took " + (endTime - startTime) + "ms)");

            return new AsyncResult<>("Notification Sent Successfully");
        } catch (InterruptedException e) {
            return new AsyncResult<>("Notification Failed");
        }
    }
}