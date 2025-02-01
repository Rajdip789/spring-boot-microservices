package com.rajdip14.ecommerce.customer;

import com.rajdip14.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(CustomerRequest request) {
        var customer = customerRepository.save(customerMapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        var customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Cannot update customer:: No customer found with ID:: %s", request.id())
                ));

        mergeCustomer(customer, request);

        customerRepository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        Optional.ofNullable(request.firstname()).ifPresent(customer::setFirstname);
        Optional.ofNullable(request.lastname()).ifPresent(customer::setLastname);
        Optional.ofNullable(request.email()).ifPresent(customer::setEmail);
        Optional.ofNullable(request.address()).ifPresent(customer::setAddress);
    }

    public List<CustomerResponse> findAllCustomers(Integer pageNumber, String sortKey, String sortOrder) {

        pageNumber = pageNumber < 0 ? 0 : pageNumber;
        sortKey = List.of("id", "firstname", "email").contains(sortKey) ? sortKey : "id"; // Allowed sort keys
        sortOrder = sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc") ? sortOrder.toLowerCase() : "asc";

        PageRequest pageRequest = PageRequest.of(pageNumber, 15, Sort.by(Sort.Direction.fromString(sortOrder), sortKey));

        return customerRepository.findAll(pageRequest)
                .stream()
                .map(customerMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return customerRepository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("No customer found with ID:: %s", customerId)
                ));
    }

    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }
        customerRepository.deleteById(customerId);
    }
}
