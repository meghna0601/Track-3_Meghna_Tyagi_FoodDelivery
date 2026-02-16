package com.user.model;

import com.user.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String address;
    private Boolean active;
    private String phoneNumber;
    private Role role;
}
