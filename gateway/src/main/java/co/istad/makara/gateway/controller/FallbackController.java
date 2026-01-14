package co.istad.makara.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Fallback Controller
 * Handles requests when services are down (Circuit Breaker pattern)
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/products")
    public ResponseEntity<Map<String, String>> productFallback() {
        return ResponseEntity.ok(Map.of(
                "message", "Product Service is currently unavailable. Please try again later."
        ));
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, String>> categoryFallback() {
        return ResponseEntity.ok(Map.of(
                "message", "Category Service is currently unavailable. Please try again later."
        ));
    }
}