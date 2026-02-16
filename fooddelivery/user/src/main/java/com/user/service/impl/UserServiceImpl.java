package com.user.service.impl;

import com.user.dto.*;
import com.user.enums.Role;
import com.user.exception.ResourceAlreadyExistException;
import com.user.exception.ResourceNotFoundException;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import com.user.util.PasswordEncryptionDecryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse newUser(UserRequest userRequest) {
        log.info("Enter in UserService creating user");
         Optional<User> userOptional = userRepository.findByEmail(userRequest.email());
        if(!userOptional.isPresent()) {
            String userId = UUID.randomUUID().toString();
            userRepository.save(User.builder()
                    .id(userId)
                    .name(userRequest.name())
                    .email(userRequest.email())
                    .role(Role.CUSTOMER)
                    .active(true)
                    .password(PasswordEncryptionDecryptionUtil.encrypt(userRequest.password()))
                    .phoneNumber(userRequest.phoneNumber())
                    .address(userRequest.address())
                    .build());
            return UserResponse.builder().userId(userId).message("Successfully added")
            .build();

        }

        if(!userOptional.get().getActive()) {
          User user = userOptional.get();
           user = user.toBuilder().active(true).build();
          userRepository.save(user);
            return UserResponse.builder().userId(userOptional.get().getId()).message("Successfully created").build();
        }
        throw new ResourceAlreadyExistException("Duplicate User cannot be added");
    }

    @Override
    public UserUpdateResponse updateUser(UserRequest userRequest) {
        log.info("Enter in UserService updating user");
        Optional<User> userOptional = userRepository.findByEmailAndActive(userRequest.email(),true);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user = user.toBuilder().email(userRequest.email())
                            .address(userRequest.address())
                                    .phoneNumber(userRequest.phoneNumber())
                                            .name(userRequest.name()).build();

                    userRepository.save(user);


            return UserUpdateResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(Role.valueOf(user.getRole().name()))
                    .phoneNumber(userRequest.phoneNumber())
                    .address(userRequest.address())
                    .build();

        }
        throw new ResourceNotFoundException("User Not Found with the given details");
    }

    @Override
    public UserResponse deleteUser(UserDeleteRequest userDeleteRequest) {
        log.info("Enter in UserService deleting user");
        Optional<User> userOptional = userRepository.findByIdAndActive(userDeleteRequest.userId(),true);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(false);
            userRepository.save(user);
            log.info("User delete successfully");
           return UserResponse.builder()
                    .message("Deleted Successfully")
                    .userId(userDeleteRequest.userId())
                    .build();
        }
        throw new ResourceNotFoundException("User id not found");
    }

    @Override
    public UserResponse switchRole(String userId,UserRoleRequest userRequest) {
        log.info("Enter in UserService switch user role");
        Optional<User> userOptional = userRepository.findByIdAndActive(userId,true);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(Role.valueOf(userRequest.role().name()));
            userRepository.save(user);
            log.info("Role Switch Successfully");
            return UserResponse.builder()
                    .message("Role Updated Successfully")
                    .userId(userId)
                    .build();
        }
        throw new ResourceNotFoundException("User Id not found");
    }

    @Override
    public GetUserResponse getUserDetails(String userId) {
        Optional<User> userOptional = userRepository.findByIdAndActive(userId,true);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return  GetUserResponse.builder().userDetails(GetUserDetails.builder()
                            .email(user.getEmail())
                                            .address(user.getAddress())
                                                    .id(user.getId())
                                                            .name(user.getName())
                                                                    .phoneNumber(user.getPhoneNumber()).build())
                             .message("User Found").build();
        }
        throw new ResourceNotFoundException("User Not Found !!");
    }
}
