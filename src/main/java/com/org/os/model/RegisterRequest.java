package com.org.os.model;

import com.org.os.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private Integer userId;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private Role role;
}
