package com.rajdip14.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
    Integer id,
    @NotNull(message = "Product name is require")
    String name,
    @NotNull(message = "Product description is require")
    String description,
    @Positive(message = "Available quantity should be positive")
    double availableQuantity,
    @Positive(message = "Price should be positive")
    BigDecimal price,
    @NotNull(message = "Product category is required")
    Integer categoryId
) {
}

