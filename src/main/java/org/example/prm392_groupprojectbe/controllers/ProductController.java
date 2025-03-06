package org.example.prm392_groupprojectbe.controllers;

import org.example.prm392_groupprojectbe.dtos.product.request.GetProductsRequestDTO;
import org.example.prm392_groupprojectbe.entities.Product;
import org.example.prm392_groupprojectbe.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock) {

        GetProductsRequestDTO requestDTO = GetProductsRequestDTO.builder()
                .categoryId(categoryId)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .search(search)
                .minStock(minStock)
                .maxStock(maxStock)
                .pageNumber(page)
                .pageSize(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();

        Page<Product> products = productService.getByParameters(requestDTO);
        return ResponseEntity.ok(products);
    }
}