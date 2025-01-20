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

@FeignClient(
        name = "product-service"
)
public interface ProductClient {

    Logger log = LoggerFactory.getLogger(ProductClient.class);

    @PostMapping("/api/v1/products/purchase")
    @Retry(name="productServiceRetry")
    @CircuitBreaker(name = "productService", fallbackMethod = "productServiceFallback")
    ResponseEntity<List<PurchaseResponse>> purchaseProducts(@RequestBody List<PurchaseRequest> requestBody);

    default ResponseEntity<List<PurchaseResponse>> productServiceFallback(List<PurchaseRequest> products, Throwable throwable) {
        log.error("Product service fallback triggered. Reason: {}", throwable.getMessage());
        throw new BusinessException("Product service is currently unavailable. Please try again later.");
    }
}
