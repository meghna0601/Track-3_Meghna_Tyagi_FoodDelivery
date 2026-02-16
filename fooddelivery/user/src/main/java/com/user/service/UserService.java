package com.user.service;

import com.user.dto.*;

public interface UserService {
    UserResponse newUser(UserRequest userRequest);

    UserUpdateResponse updateUser(UserRequest userRequest);

    UserResponse deleteUser(UserDeleteRequest userDeleteRequest);

    UserResponse switchRole(String userId,UserRoleRequest userRequest);

    GetUserResponse getUserDetails(String userId);
}
