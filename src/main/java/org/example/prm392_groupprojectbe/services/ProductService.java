package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.product.request.CreateProductRequestDTO;
import org.example.prm392_groupprojectbe.dtos.product.request.GetProductsRequestDTO;
import org.example.prm392_groupprojectbe.dtos.product.request.UpdateProductRequestDTO;
import org.example.prm392_groupprojectbe.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> getByParameters(GetProductsRequestDTO requestDTO);

    List<Product> getAllProducts();
    Product getProductById(Long id);

    Product createProduct(CreateProductRequestDTO dto);

    Product updateProduct(Long id, UpdateProductRequestDTO dto);

    void deleteProduct(Long id);

    void updateStockAfterPayment(Product product, Integer quantity);

    void updateStockAfterOrderFailure(Product product, Integer quantity);
}