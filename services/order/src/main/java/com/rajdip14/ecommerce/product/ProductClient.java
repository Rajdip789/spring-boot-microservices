package com.rajdip14.ecommerce.product;

import com.rajdip14.ecommerce.exception.BusinessException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Feign client interface to communicate with the Product Service.
 *
 * HOW IT WORKS:
 * - @FeignClient creates a proxy for calling another service (product-service) via HTTP.
 * - The "name" attribute corresponds to the service ID in Eureka.
 * - The request is automatically load-balanced (if Eureka + Spring Cloud LoadBalancer is enabled).
 *
 * FAULT TOLERANCE:
 * - @Retry will automatically retry failed calls (network errors (if idempotent api), service down (5xx status) etc.)
 *   based on configuration in application.yml (e.g., retry count, wait time).
 * - @CircuitBreaker monitors failures and, after a threshold, "opens" to stop calling
 *   the service temporarily, returning fallback responses instead.
 * - fallbackMethod is called when the circuit is OPEN or after all retries fail.
 */
@FeignClient(
        name = "product-service"
)
public interface ProductClient {

    Logger log = LoggerFactory.getLogger(ProductClient.class);

    /**
     * Calls the product-service to purchase products.
     *
     * @param requestBody List of PurchaseRequest objects.
     * @return List of PurchaseResponse objects wrapped in ResponseEntity.
     *
     * @Retry:
     *   - Tries the request again automatically if it fails.
     *   - "productServiceRetry" corresponds to retry config in application.yml.
     *
     * @CircuitBreaker:
     *   - Monitors call failures and opens the circuit after too many errors.
     *   - When open, it calls productServiceFallback instead of hitting the service.
     */
    @PostMapping("/api/v1/products/purchase")
    @Retry(name="productServiceRetry")
    @CircuitBreaker(name = "productService", fallbackMethod = "productServiceFallback")
    ResponseEntity<List<PurchaseResponse>> purchaseProducts(@RequestBody List<PurchaseRequest> requestBody);

    default ResponseEntity<List<PurchaseResponse>> productServiceFallback(List<PurchaseRequest> products, Throwable throwable) {
        log.error("Product service fallback triggered. Reason: {}", throwable.getMessage());
        throw new BusinessException("Product service is currently unavailable. Please try again later.");
    }
}
