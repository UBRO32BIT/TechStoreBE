package org.example.prm392_groupprojectbe.repositories;

import org.example.prm392_groupprojectbe.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}