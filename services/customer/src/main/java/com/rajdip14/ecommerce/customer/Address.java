package com.rajdip14.ecommerce.customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
public class Address {
    String street;
    String houseNumber;
    @NotNull(message = "Zip code is required")
    @Pattern(regexp = "\\d{6}", message = "Pin code must be 6 digits")
    String pinCode;
}
