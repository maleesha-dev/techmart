package lk.novasphere.techmart.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.PostActivate;
import jakarta.ejb.PrePassivate;
import jakarta.ejb.Stateful;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Stateful
public class CartService implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());

    private Map<Long, Integer> cartItems;

    @PostConstruct
    public void init() {
        cartItems = new HashMap<>();
        LOGGER.info("CartService initialized...");
    }

    public void addItem(Long productId, Integer quantity) {
        cartItems.put(productId, cartItems.getOrDefault(productId, 0) + quantity);
        LOGGER.info("Item added to cart: ProductID=" + productId + ", Qty=" + quantity);
    }

    public void removeItem(Long productId) {
        cartItems.remove(productId);
        LOGGER.info("Item removed from cart: ProductID=" + productId);
    }

    public Map<Long, Integer> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
        LOGGER.info("Cart has been cleared.");
    }

    //OPTIMIZATION
    @PrePassivate
    public void prePassivate() {

        LOGGER.info("@PrePassivate: Cart is being saved to storage to save memory.");
    }

    @PostActivate
    public void postActivate() {
        LOGGER.info("@PostActivate: Cart has been restored back to memory.");
    }

    @PreDestroy
    public void destroy() {
        LOGGER.info("@PreDestroy: CartService Bean is being destroyed.");
    }
}