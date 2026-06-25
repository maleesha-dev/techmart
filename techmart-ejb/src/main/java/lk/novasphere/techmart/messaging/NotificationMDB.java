package lk.novasphere.techmart.messaging;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import java.util.logging.Logger;

@MessageDriven(name = "NotificationMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "TechMartOrderQueue"),
        @ActivationConfigProperty(propertyName = "resourceAdapter", propertyValue = "activemq-rar-6.2.6"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "maxSessions", propertyValue = "10")
})
public class NotificationMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(NotificationMDB.class.getName());

    @PostConstruct
    public void init() {
        
        System.out.println("Instance Created & Ready to Consume!");
        LOGGER.info("MDB Instance Created & Ready to Consume Messages!");
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String payload = textMessage.getText();

                System.out.println("\n==========================================================");
                System.out.println("[NOTIFICATION SERVICE] : ActiveMQ Asynchronous Notification");
                System.out.println("[Received Payload] : " + payload);
                System.out.println("==========================================================\n");
            }
        } catch (Exception e) {
            LOGGER.severe("CRITICAL ERROR in MDB processing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        LOGGER.info("Instance is being Destroyed ");
    }
}