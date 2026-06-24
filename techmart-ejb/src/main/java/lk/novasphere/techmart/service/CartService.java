package lk.novasphere.techmart.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped; // 💡 වෙනස් කරන ලදී
import jakarta.inject.Named; // 💡 වෙනස් කරන ලදී
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Named
@SessionScoped
public class CartService implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());

    private Map<Long, Integer> cartItems;

    @PostConstruct
    public void init() {
        cartItems = new HashMap<>();
        LOGGER.info("CartService initialized for session...");
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

    @PreDestroy
    public void destroy() {
        LOGGER.info("CartService Bean is being destroyed.");
    }
}