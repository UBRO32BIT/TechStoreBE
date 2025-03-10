package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.product.request.CreateProductRequestDTO;
import org.example.prm392_groupprojectbe.dtos.product.request.GetProductsRequestDTO;
import org.example.prm392_groupprojectbe.dtos.product.request.UpdateProductRequestDTO;
import org.example.prm392_groupprojectbe.entities.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getByParameters(GetProductsRequestDTO requestDTO);

    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);

    Product createProduct(CreateProductRequestDTO dto);

    Product updateProduct(Long id, UpdateProductRequestDTO dto);

    void deleteProduct(Long id);
}