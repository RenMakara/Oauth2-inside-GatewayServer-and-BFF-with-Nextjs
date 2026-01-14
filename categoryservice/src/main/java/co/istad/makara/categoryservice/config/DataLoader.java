package co.istad.makara.categoryservice.config;

import co.istad.makara.categoryservice.entity.Category;
import co.istad.makara.categoryservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data Loader
 * Loads sample categories into the database when the application starts
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        categoryRepository.deleteAll();

        // Load sample categories
        categoryRepository.save(new Category(null, "Electronics",
                "Electronic devices and gadgets"));
        categoryRepository.save(new Category(null, "Books",
                "Books and educational materials"));
        categoryRepository.save(new Category(null, "Clothing",
                "Fashion and apparel"));
        categoryRepository.save(new Category(null, "Home & Garden",
                "Home improvement and gardening supplies"));

        System.out.println("✅ Category sample data loaded successfully!");
    }
}