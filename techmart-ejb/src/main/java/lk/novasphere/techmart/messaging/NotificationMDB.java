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
        @ActivationConfigProperty(propertyName = "resourceAdapter", propertyValue = "activemq-rar-6.2.6"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "OrderQueue"),
        @ActivationConfigProperty(propertyName = "maxSessions", propertyValue = "10")
})
public class NotificationMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(NotificationMDB.class.getName());

    @PostConstruct
    public void init() {
        LOGGER.info("Instance Created & Ready to Consume Messages ");
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String payload = textMessage.getText();

                LOGGER.info("🟢 Received Message from Queue: " + payload);

                // පරණ හිරවුණු "Test 1" වගේ Format එක නැති මැසේජ් ආවොත් Skip කිරීමට
                if (payload == null || !payload.contains("|")) {
                    LOGGER.warning("⚠️ Invalid or legacy message format received. Skipping parsing.");
                    return;
                }

                String orderId = "N/A";
                String customer = "N/A";
                String amount = "N/A";

                String[] parts = payload.split("\\|");
                for (String part : parts) {
                    if (part.contains(":")) {
                        String key = part.substring(0, part.indexOf(":")).trim().toLowerCase();
                        String value = part.substring(part.indexOf(":") + 1).trim();

                        if (key.equals("orderid")) {
                            orderId = value;
                        } else if (key.equals("customer")) {
                            customer = value;
                        } else if (key.equals("amount")) {
                            amount = value;
                        }
                    }
                }

                System.out.println("==========================================================");
                System.out.println("📢 [NOTIFICATION SERVICE] ActiveMQ Asynchronous Notification");
                System.out.println("✉️ Sending confirmation email to: " + customer);
                System.out.println("🆔 Order ID: " + orderId + " successfully processed via MDB.");
                System.out.println("💰 Total Bill: Rs. " + amount);
                System.out.println("==========================================================");
            }
        } catch (Exception e) {
            LOGGER.severe("🔴 CRITICAL ERROR in MDB processing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        LOGGER.info("Instance is being Destroyed ");
    }
}