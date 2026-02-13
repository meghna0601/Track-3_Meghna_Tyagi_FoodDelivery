package com.fooddelivery.model;

import com.fooddelivery.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder(toBuilder = true)
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
