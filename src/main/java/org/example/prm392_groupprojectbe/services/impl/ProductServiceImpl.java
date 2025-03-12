package org.example.prm392_groupprojectbe.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.product.request.CreateProductRequestDTO;
import org.example.prm392_groupprojectbe.dtos.product.request.GetProductsRequestDTO;
import org.example.prm392_groupprojectbe.dtos.product.request.UpdateProductRequestDTO;
import org.example.prm392_groupprojectbe.entities.Product;
import org.example.prm392_groupprojectbe.entities.ProductCategory;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.repositories.ProductCategoryRepository;
import org.example.prm392_groupprojectbe.repositories.ProductRepository;
import org.example.prm392_groupprojectbe.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    @Override
    public List<Product> getByParameters(GetProductsRequestDTO requestDTO) {
        List<String> allowedSortFields = List.of("id", "name", "price", "stock", "category", "createdAt", "updatedAt");
        if (!allowedSortFields.contains(requestDTO.getSortBy())) {
            requestDTO.setSortBy("createdAt");
        }

        Sort sort = Sort.by(requestDTO.getDirection(), requestDTO.getSortBy());

        return productRepository.findByParameters(
                requestDTO.getCategoryId(),
                requestDTO.getMinPrice(),
                requestDTO.getMaxPrice(),
                requestDTO.getSearch(),
                requestDTO.getMinStock(),
                requestDTO.getMaxStock(),
                sort
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public Product createProduct(CreateProductRequestDTO dto) {
        ProductCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(category);
        product.setImageUrl(dto.getImageUrl());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, UpdateProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        ProductCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(category);
        product.setImageUrl(dto.getImageUrl());

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}