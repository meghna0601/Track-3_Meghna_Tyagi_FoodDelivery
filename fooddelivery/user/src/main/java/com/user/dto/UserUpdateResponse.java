package com.user.dto;

import com.user.enums.Role;
import lombok.Builder;

@Builder(toBuilder = true)
public record UserUpdateResponse (

     String id,
     String name,
     String email,
     String address,
     String phoneNumber,
     Role role) {
}
