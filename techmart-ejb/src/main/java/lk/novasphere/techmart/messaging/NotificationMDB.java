package lk.novasphere.techmart.messaging;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/OrderQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),

        @ActivationConfigProperty(propertyName = "maxPoolSize", propertyValue = "10"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class NotificationMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(NotificationMDB.class.getName());

    @PostConstruct
    public void init() {
        LOGGER.info("NotificationMDB Instance Created & Ready to Consume Messages");
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String payload = textMessage.getText();

                LOGGER.info("MDB Received Message from Queue: " + payload);

                String[] parts = payload.split(":");
                String orderId = parts[0];
                String details = parts[1];

                LOGGER.info("Notification processed for Order ID: " + orderId + ". Content: " + details);
            }
        } catch (Exception e) {
            LOGGER.severe("Error processing message in MDB: " + e.getMessage());
        }
    }

    @PreDestroy
    public void destroy() {
        LOGGER.info("NotificationMDB Instance is being Destroyed");
    }
}