package com.rajdip14.ecommerce.order;

import com.rajdip14.ecommerce.customer.CustomerClient;
import com.rajdip14.ecommerce.customer.CustomerResponse;
import com.rajdip14.ecommerce.exception.BusinessException;
import com.rajdip14.ecommerce.kafka.PaymentRequest;
import com.rajdip14.ecommerce.kafka.PaymentRequestProducer;
import com.rajdip14.ecommerce.orderLine.OrderLineRequest;
import com.rajdip14.ecommerce.orderLine.OrderLineService;
import com.rajdip14.ecommerce.product.ProductClient;
import com.rajdip14.ecommerce.product.PurchaseRequest;
import com.rajdip14.ecommerce.product.PurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;

    private final PaymentRequestProducer paymentRequestProducer;

    public CustomerResponse validateCustomer(String customerId) {
        return this.customerClient.findCustomerById(customerId)
                .orElseThrow(() -> new BusinessException(
                        format("Cannot create order:: No Customer exists with the provided id:: %s", customerId)
                ));
    }

    public void purchaseProducts(List<PurchaseRequest> products) {
        ResponseEntity<List<PurchaseResponse>> response = this.productClient.purchaseProducts(products);

        if (response.getStatusCode().isError()) {
            throw new BusinessException("Error occurred: HTTP status " + response.getStatusCode());
        }
    }

    public OrderResponse createOrder(OrderRequest request) {
        // check if the customer is valid --> customer-service
        CustomerResponse customer = validateCustomer(request.customerId());

        log.info(customer.toString());

        // purchase the product --> product-service
        purchaseProducts(request.products());

        // persist the order
        var order = this.orderRepository.save(orderMapper.toOrder(request));

        // persist the order lines
        for(PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // start payment process --> payment-service (kafka)
        paymentRequestProducer.sendPaymentRequest(
                new PaymentRequest(
                    request.amount(),
                    request.paymentMethod(),
                    order.getId(),
                    order.getReference(),
                    customer
                )
        );

        return orderMapper.fromOrder(order);
    }

    public List<OrderResponse> findAllOrders() {
        return this.orderRepository.findAll()
                .stream()
                .map(this.orderMapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return this.orderRepository.findById(id)
                .map(this.orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}
