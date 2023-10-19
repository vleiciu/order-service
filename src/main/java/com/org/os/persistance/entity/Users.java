package com.org.os.persistance.entity;

import com.org.os.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.YesNoConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "USERS")
public class Users implements UserDetails {

    @Id
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASS")
    private String pass;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "IS_ACTIVE")
    @Convert(converter = YesNoConverter.class)
    private Boolean isActive;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "PAYMENT_INFO")
    private String paymentInfo;

    @Enumerated
    private Role role;

    @OneToMany(mappedBy = "users")
    private List<Token> tokens;

    @OneToMany(mappedBy = "userOrders")
    private List<Order> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthority();
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
