package org.example.prm392_groupprojectbe.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractEntity<T extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    T id;

    @Column(name = "created_by")
    @CreatedBy
    String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    String updatedBy;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    boolean isDeleted;
}