package com.org.os.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum Role {
    ADMIN,
    MANAGER,
    USER;

    public List<SimpleGrantedAuthority> getAuthority() {
        return List.of(new SimpleGrantedAuthority(this.name()));
    }
}
