package org.example.prm392_groupprojectbe.repositories;

import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @Query("""
    SELECT a FROM Account a WHERE 
    (:search IS NULL OR a.name LIKE CONCAT('%', :search, '%') OR a.email LIKE CONCAT('%', :search, '%'))
    AND (:role IS NULL OR a.role = :role)
    AND (:status IS NULL OR a.status = :status)
""")
    Page<Account> findByFilters(@Param("search") String search,
                                @Param("role") AccountRoleEnum role,
                                @Param("status") AccountStatusEnum status,
                                Pageable pageable);

}
