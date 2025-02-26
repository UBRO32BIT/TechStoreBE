package org.example.prm392_groupprojectbe.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory extends AbstractEntity<Long> {
    @Column(nullable = false, length = 100)
    private String name;
}
