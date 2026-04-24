package com.learn.backend.product;

import com.learn.backend.product.dto.ProductRequest;
import com.learn.backend.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse save(ProductRequest productRequest) {
        Product product = this.productMapper.toProduct(productRequest);
        Product newProduct = this.productRepository.save(product);
        return this.productMapper.toProductResponse(newProduct);
    }

    public List<ProductResponse> findAll() {
        return this.productRepository.findAll().stream()
                .map(this.productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse findProductById(Long id) {
        return this.productRepository.findById(id)
                .map(this.productMapper::toProductResponse)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void reduceStock(Long id, Integer quantity) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setStock(product.getStock() - quantity);
        this.productRepository.save(product);
    }
}
