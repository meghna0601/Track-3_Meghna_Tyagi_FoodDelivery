package com.user.dto;

import com.user.enums.Role;
import lombok.Builder;

@Builder(toBuilder = true)
public record UserRoleRequest(
        Role role
) {
}
