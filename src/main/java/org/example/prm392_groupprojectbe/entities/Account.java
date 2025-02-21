package org.example.prm392_groupprojectbe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.prm392_groupprojectbe.enums.AccountGenderEnum;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "account")
public class Account extends AbstractEntity<Long> implements UserDetails {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRoleEnum role;

    @Column
    private String externalAuthId;

    @Column(nullable = true, length = 1000)
    private String avatar;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatusEnum status;

    @Column(nullable = true)
    private AccountGenderEnum gender;

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(this.role.name()));
        return authorities;
    }

    @Transient
    private String accessToken;

    @Transient
    @Override
    public String getUsername() {
        return this.email;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
