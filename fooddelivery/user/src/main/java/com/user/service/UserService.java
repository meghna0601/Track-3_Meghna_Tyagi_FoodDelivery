package com.user.service;

import com.user.dto.UserRequest;
import com.user.dto.UserResponse;
import com.user.dto.UserUpdateResponse;

public interface UserService {
    UserResponse newUser(UserRequest userRequest);

    UserUpdateResponse updateUser(UserRequest userRequest);
}
