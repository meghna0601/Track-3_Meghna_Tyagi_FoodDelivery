package com.user.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserRequest(
         String name,
         String email,
         String password,
         String address,
         Boolean active,
         String phoneNumber
) {

}
