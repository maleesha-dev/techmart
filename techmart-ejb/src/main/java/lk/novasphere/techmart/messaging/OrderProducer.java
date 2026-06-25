package lk.novasphere.techmart.messaging;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.*;

import java.util.logging.Logger;

@Stateless
public class OrderProducer {

    private static final Logger LOGGER = Logger.getLogger(OrderProducer.class.getName());


    @Resource(lookup = "jms/TechMartConnectionFactory")
    private ConnectionFactory connectionFactory;


    @Resource(lookup = "jms/OrderQueue")
    private Queue queue;

    public void sendOrderMessage(Long orderId, String customerEmail, double amount) {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);

            String fullMessage = "OrderId:" + orderId + "|Customer:" + customerEmail + "|Amount:" + amount;
            TextMessage textMessage = session.createTextMessage(fullMessage);

            producer.send(textMessage);
            LOGGER.info("Message successfully pushed to OrderQueue for Order #" + orderId);

        } catch (Exception e) {
            LOGGER.severe("Message sending failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    LOGGER.severe("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }
}