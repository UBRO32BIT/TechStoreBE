package org.example.prm392_groupprojectbe.controllers;

import org.example.prm392_groupprojectbe.dtos.productscategory.ProductCategoryDTO;
import org.example.prm392_groupprojectbe.entities.ProductCategory;
import org.example.prm392_groupprojectbe.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public List<ProductCategoryDTO> getAllCategories() {
        return productCategoryService.getAllCategories()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> getCategoryById(@PathVariable Long id) {
        ProductCategory category = productCategoryService.getCategoryById(id);
        return category != null ? ResponseEntity.ok(convertToDTO(category)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ProductCategoryDTO createCategory(@RequestBody ProductCategoryDTO categoryDTO) {
        ProductCategory category = new ProductCategory();
        category.setName(categoryDTO.getName());
        ProductCategory createdCategory = productCategoryService.createCategory(category);
        return convertToDTO(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> updateCategory(@PathVariable Long id, @RequestBody ProductCategoryDTO categoryDTO) {
        ProductCategory category = new ProductCategory();
        category.setId(id);
        category.setName(categoryDTO.getName());
        ProductCategory updatedCategory = productCategoryService.updateCategory(id, category);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        productCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    private ProductCategoryDTO convertToDTO(ProductCategory category) {
        ProductCategoryDTO dto = new ProductCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}