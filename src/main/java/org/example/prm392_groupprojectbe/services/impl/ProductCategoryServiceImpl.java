package org.example.prm392_groupprojectbe.services.impl;

import org.example.prm392_groupprojectbe.entities.ProductCategory;
import org.example.prm392_groupprojectbe.repositories.ProductCategoryRepository;
import org.example.prm392_groupprojectbe.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public ProductCategory getCategoryById(Long id) {
        return productCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public ProductCategory createCategory(ProductCategory category) {
        return productCategoryRepository.save(category);
    }

    @Override
    public ProductCategory updateCategory(Long id, ProductCategory category) {
        category.setId(id);
        return productCategoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }
}