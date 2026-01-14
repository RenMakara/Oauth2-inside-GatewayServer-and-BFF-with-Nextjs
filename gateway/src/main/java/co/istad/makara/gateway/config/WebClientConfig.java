package co.istad.makara.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient Configuration
 * WebClient is used for making non-blocking HTTP calls to microservices
 * This is part of Spring WebFlux reactive programming model
 */
@Configuration
public class WebClientConfig {
    /**
     * Create a WebClient bean with base configuration
     * This will be used to make HTTP calls to our microservices
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}