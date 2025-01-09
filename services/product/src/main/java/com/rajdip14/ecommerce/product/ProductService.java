package com.rajdip14.ecommerce.product;

import com.rajdip14.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        // 1. Get the id's of requested products
        List<Integer> productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        // 2. Find the products with the requested id's and order them
        List<Product> storedProducts = productRepository.findAllByIdInOrderById(productIds);

        // 3. Check if all products are available
        if(productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exists");
        }

        // 4. Sort the requested products by id's
        List<ProductPurchaseRequest> sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        List<ProductPurchaseResponse> purchasedProducts = new ArrayList<>();

        // 5. For all products check if requested quantity is available, and then update quantity & product, else throw exception
        for(int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);

            if(product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException("Insufficient stock quantity for product with id:: " + productRequest.productId());
            }

            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();

            product.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(product);

            purchasedProducts.add(productMapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }

        return purchasedProducts;
    }

    public ProductResponse findById(Integer productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with the id:: " + productId));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
