package lk.novasphere.techmart.service;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.novasphere.techmart.entity.Order;
import lk.novasphere.techmart.entity.Product;

import java.util.Map;


@JMSConnectionFactoryDefinition(
        name = "java:app/jms/ActiveMQConnectionFactory",
        resourceAdapter = "activemq-rar-6.2.6"
)
@Stateless
public class OrderService {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private InventoryCache inventoryCache;


    @Resource(lookup = "jms/__defaultConnectionFactory")
    private ConnectionFactory activeMQFactory;

    @Resource(lookup = "jms/OrderQueue")
    private Queue notificationQueue;

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
                String messagePayload = "OrderID:" + order.getId() + "|Customer:" + customerName + "|Amount:" + totalAmount;


                try (JMSContext jmsContext = activeMQFactory.createContext()) {
                    jmsContext.createProducer().send(notificationQueue, messagePayload);
                }

                System.out.println("Order success message sent to OrderQueue: " + messagePayload);

            } catch (Exception e) {
                System.err.println("Producer Error: " + e.getMessage());
            }

        } else {
            order.setStatus("FAILED");
        }

        return order;
    }
}