package org.example.prm392_groupprojectbe.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.prm392_groupprojectbe.enums.PaymentMethod;
import org.example.prm392_groupprojectbe.enums.PaymentStatus;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Payment extends AbstractEntity<Long> {
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(length = 100)
    private String transactionId;
}
