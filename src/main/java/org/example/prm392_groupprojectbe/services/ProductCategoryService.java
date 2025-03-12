package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.entities.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getAllCategories();
    ProductCategory getCategoryById(Long id);
    ProductCategory createCategory(ProductCategory category);
    ProductCategory updateCategory(Long id, ProductCategory category);
    void deleteCategory(Long id);
}