package com.rajdip14.ecommerce.customer;

import com.rajdip14.ecommerce.exception.BusinessException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "customer-service"
)
public interface CustomerClient {

    Logger log = LoggerFactory.getLogger(CustomerClient.class);

    @GetMapping("/api/v1/customers/{customer-id}")
    @CircuitBreaker(name = "customerService", fallbackMethod = "customerServiceFallback")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId);

    default Optional<CustomerResponse> customerServiceFallback(String customerId, Throwable throwable) {
        log.info("Customer service fallback triggered. Reason: {}", throwable.getMessage());
        throw new BusinessException("Customer service is currently unavailable. Please try again later.");
    }
}