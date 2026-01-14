package co.istad.makara.productservice.config;

import co.istad.makara.productservice.entity.Product;
import co.istad.makara.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data Loader
 * Loads sample data into the database when the application starts
 * This is useful for demo and testing purposes
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        productRepository.deleteAll();

        // Load sample products
        // Category 1: Electronics
        productRepository.save(new Product(null, "Laptop", 999.99,
                "https://images.unsplash.com/photo-1496181133206-80ce9b88a853", 1L));
        productRepository.save(new Product(null, "Smartphone", 699.99,
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9", 1L));
        productRepository.save(new Product(null, "Tablet", 449.99,
                "https://images.unsplash.com/photo-1561154464-82e9adf32764", 1L));

        // Category 2: Books
        productRepository.save(new Product(null, "Spring Boot in Action", 39.99,
                "https://images.unsplash.com/photo-1532012197267-da84d127e765", 2L));
        productRepository.save(new Product(null, "Clean Code", 44.99,
                "https://images.unsplash.com/photo-1544947950-fa07a98d237f", 2L));

        // Category 3: Clothing
        productRepository.save(new Product(null, "T-Shirt", 19.99,
                "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab", 3L));
        productRepository.save(new Product(null, "Jeans", 59.99,
                "https://images.unsplash.com/photo-1542272604-787c3835535d", 3L));

        System.out.println("✅ Product sample data loaded successfully!");
    }
}
