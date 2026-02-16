package com.user.dto;

import com.user.enums.Role;
import lombok.Builder;

@Builder(toBuilder = true)
public record GetUserDetails(
         String id,
         String name,
         String email,
         String address,
         String phoneNumber

) {
}
