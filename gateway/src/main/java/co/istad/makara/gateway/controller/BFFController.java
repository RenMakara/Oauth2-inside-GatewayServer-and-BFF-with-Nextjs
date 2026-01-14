package co.istad.makara.gateway.controller;

import co.istad.makara.gateway.dtos.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * BFF (Backend-for-Frontend) Controller
 *
 * This controller demonstrates the BFF pattern by:
 * 1. Calling multiple microservices (Product and Category)
 * 2. Aggregating their responses
 * 3. Returning a single, frontend-optimized response
 *
 * Benefits of BFF:
 * - Reduces number of frontend API calls (1 instead of 2)
 * - Frontend gets exactly the data it needs
 * - Can add frontend-specific logic here
 * - Improved performance through parallel calls
 */
@RestController
@RequestMapping("/bff")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow CORS for frontend
public class BFFController {

    private final WebClient.Builder webClientBuilder;

    /**
     * Dashboard endpoint - Aggregates data from multiple services
     *
     * GET /bff/dashboard
     *
     * This endpoint:
     * 1. Makes parallel calls to Product Service and Category Service
     * 2. Uses Mono.zip to combine the results reactively
     * 3. Returns aggregated data in a single response
     */
    @GetMapping("/dashboard")
    public Mono<DashboardResponse> getDashboardData() {

        // Create WebClient for Product Service
        WebClient productClient = webClientBuilder
                .baseUrl("http://localhost:8081")
                .build();

        // Create WebClient for Category Service
        WebClient categoryClient = webClientBuilder
                .baseUrl("http://localhost:8082")
                .build();

        // Make parallel non-blocking calls to both services
        Mono<List<DashboardResponse.Product>> productsMono = productClient
                .get()
                .uri("/api/products")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<DashboardResponse.Product>>() {})
                .doOnError(error -> System.err.println("Error fetching products: " + error.getMessage()))
                .onErrorReturn(List.of()); // Return empty list on error

        Mono<List<DashboardResponse.Category>> categoriesMono = categoryClient
                .get()
                .uri("/api/categories")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<DashboardResponse.Category>>() {})
                .doOnError(error -> System.err.println("Error fetching categories: " + error.getMessage()))
                .onErrorReturn(List.of()); // Return empty list on error

        // Combine both results using Mono.zip
        // This executes both calls in parallel and waits for both to complete
        return Mono.zip(productsMono, categoriesMono)
                .map(tuple -> new DashboardResponse(
                        tuple.getT1(), // products
                        tuple.getT2()  // categories
                ));
    }
}