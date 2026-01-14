package co.istad.makara.gateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * API Gateway Route Configuration using RouteLocator
 *
 * Routes configured:
 * 1. Product Service: /api/products/** → http://localhost:8081
 * 2. Category Service: /api/categories/** → http://localhost:8082
 * 3. BFF Endpoint: /bff/** → handled by BffController
 * 4. Next.js Frontend: /** → http://localhost:3000
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                // ============================================================
                // ROUTE 1: Product Service
                // ============================================================
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("productServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/products")
                                )
                                .filter((exchange, chain) -> {
                                    System.out.println("🔵 [PRODUCT-SERVICE] " +
                                            exchange.getRequest().getMethod() + " " +
                                            exchange.getRequest().getURI().getPath());
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("http://localhost:8081")
                )

                // ============================================================
                // ROUTE 2: Category Service
                // ============================================================
                .route("category-service", r -> r
                        .path("/api/categories/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("categoryServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/categories")
                                )
                                .filter((exchange, chain) -> {
                                    System.out.println("🟢 [CATEGORY-SERVICE] " +
                                            exchange.getRequest().getMethod() + " " +
                                            exchange.getRequest().getURI().getPath());
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("http://localhost:8082")
                )

                // ============================================================
                // ROUTE 3: BFF Endpoints (MUST come before Next.js route)
                // ============================================================
                .route("bff-service", r -> r
                        .path("/bff/**")
                        .filters(f -> f
                                .filter((exchange, chain) -> {
                                    System.out.println("🟡 [BFF] " +
                                            exchange.getRequest().getMethod() + " " +
                                            exchange.getRequest().getURI().getPath());
                                    return chain.filter(exchange);
                                })
                        )
                        // URI is not needed here as BFF is handled locally by BffController
                        .uri("no://op")
                )

                // ============================================================
                // ROUTE 4: Next.js Frontend (MUST be last)
                // This route catches all remaining requests-
                // ============================================================
                .route("nextjs-frontend", r -> r
                        .path("/**")
                        .filters(f -> f
                                .filter((exchange, chain) -> {
                                    String path = exchange.getRequest().getURI().getPath();
                                    System.out.println("⚛️  [NEXT.JS] " +
                                            exchange.getRequest().getMethod() + " " + path);
                                    return chain.filter(exchange);
                                })
                        )
                        // Forward all frontend requests to Next.js dev server
                        .uri("http://localhost:3000")
                )

                .build();
    }
}