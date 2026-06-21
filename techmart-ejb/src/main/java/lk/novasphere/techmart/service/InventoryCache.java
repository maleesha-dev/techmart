package lk.novasphere.techmart.service;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class InventoryCache {

    private final Map<Long, Integer> stockCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {    //Sample data
        stockCache.put(1L, 50);
        stockCache.put(2L, 100);
        System.out.println("InventoryCache Initialized With Sample Data...");
    }

    @Lock(LockType.READ)
    public Integer getStock(Long productId) {
        return stockCache.getOrDefault(productId, 0);
    }

    @Lock(LockType.WRITE)
    public void updateStock(Long productId, Integer quantity) {
        stockCache.put(productId, quantity);
    }

    @Lock(LockType.WRITE)
    public boolean deductStock(Long productId, Integer quantity) {
        Integer currentStock = stockCache.getOrDefault(productId, 0);
        if (currentStock >= quantity) {
            stockCache.put(productId, currentStock - quantity);
            return true;
        }
        return false;
    }
}