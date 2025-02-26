package org.example.prm392_groupprojectbe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.prm392_groupprojectbe.enums.Rating;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Review extends AbstractEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Account user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rating rating;

    @Column(length = 1000)
    private String comment;
}
