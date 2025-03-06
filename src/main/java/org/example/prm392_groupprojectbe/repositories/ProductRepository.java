package org.example.prm392_groupprojectbe.repositories;

import org.example.prm392_groupprojectbe.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}