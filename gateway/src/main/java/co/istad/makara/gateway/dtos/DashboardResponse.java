package co.istad.makara.gateway.dtos;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dashboard Response DTO
 * This is the aggregated response returned by the BFF endpoint
 * It combines data from multiple microservices into one response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private List<Product> products;
    private List<Category> categories;

    /**
     * Product DTO
     * Matches the Product entity from Product Service
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        private Long id;
        private String name;
        private Double price;
        private String imageUrl;
        private Long categoryId;
    }

    /**
     * Category DTO
     * Matches the Category entity from Category Service
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private Long id;
        private String name;
        private String description;
    }
}