package lk.novasphere.techmart.messaging;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import java.util.logging.Logger;

@Stateless
public class OrderProducer {

    private static final Logger LOGGER = Logger.getLogger(OrderProducer.class.getName());

    @Resource(lookup = "jms/TechMartConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/OrderQueue")
    private Queue queue;

    public void sendOrderMessage(Long orderId, String messageBody) {
        try {
            JMSContext context = connectionFactory.createContext();

            String fullMessage = orderId + ":" + messageBody;
            context.createProducer().send(queue, fullMessage);

            LOGGER.info("Message successfully pushed to OrderQueue for Order #" + orderId);
        } catch (Exception e) {
            LOGGER.severe("Message sending failed: " + e.getMessage());
        }
    }
}