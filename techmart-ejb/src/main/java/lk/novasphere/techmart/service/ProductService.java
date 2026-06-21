package lk.novasphere.techmart.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.novasphere.techmart.entity.Product;
import java.util.List;

@Stateless
public class ProductService {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Inject
    private InventoryCache inventoryCache;

    public Product createProduct(Product product) {
        em.persist(product);
        em.flush();

        inventoryCache.updateStock(product.getId(), product.getStock());
        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();

        for (Product p : products) {
            p.setStock(inventoryCache.getStock(p.getId()));
        }
        return products;
    }

    public Product getProductById(Long id) {
        Product product = em.find(Product.class, id);
        if (product != null) {
            product.setStock(inventoryCache.getStock(id));
        }
        return product;
    }
}