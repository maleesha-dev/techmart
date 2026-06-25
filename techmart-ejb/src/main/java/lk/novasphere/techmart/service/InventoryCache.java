package lk.novasphere.techmart.service;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class InventoryCache {

    private static final Logger LOGGER = Logger.getLogger(InventoryCache.class.getName());

    private final Map<Long, Integer> stockCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        LOGGER.info("[LIFECYCLE] : InventoryCache EJB Initialized at Startup.");
    }

    @Lock(LockType.READ)
    public Integer getStock(Long productId) {
        return stockCache.getOrDefault(productId, 0);
    }

    @Lock(LockType.WRITE)
    public void updateStock(Long productId, Integer quantity) {
        long startTime = System.nanoTime(); //Performance Metric

        stockCache.put(productId, quantity);

        long duration = System.nanoTime() - startTime;
        LOGGER.info("[CACHE METRIC] Stock updated for Product " + productId + " to " + quantity + " (Took " + duration + " ns)"); //
    }

    @Lock(LockType.WRITE)
    public boolean deductStock(Long productId, Integer quantity) {
        long startTime = System.currentTimeMillis(); //Response time

        Integer currentStock = stockCache.getOrDefault(productId, 0);
        if (currentStock >= quantity) {
            stockCache.put(productId, currentStock - quantity);

            long endTime = System.currentTimeMillis();
            LOGGER.info("[PERFORMANCE METRIC] : deductStock executed in " + (endTime - startTime) + " ms");
            return true;
        }

        return false;
    }
}