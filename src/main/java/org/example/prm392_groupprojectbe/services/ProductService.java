package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductServiceInterface {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product productDetails);
    void deleteProduct(Long id);
}