package com.user.controller;

import com.user.dto.*;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> newUser(@RequestBody UserRequest userRequest) {
        log.info("Enter in UserController creating user");
        UserResponse userResponse = userService.newUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<UserUpdateResponse> updateUserDetails(@RequestBody UserRequest userRequest) {
        log.info("Enter in UserController updating user");
        UserUpdateResponse userResponse = userService.updateUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<UserResponse> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest) {
        log.info("Enter in UserController delete user");
        UserResponse userResponse = userService.deleteUser(userDeleteRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("/role")
    public ResponseEntity<UserResponse> updateRole(@RequestParam("userId") String userId, @RequestBody UserRoleRequest userRequest) {
        log.info("Enter in UserController update role");
        UserResponse userResponse = userService.switchRole(userId,userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<GetUserResponse> getUser(@RequestParam String userId) {
        log.info("Enter in UserController update role");
        GetUserResponse userResponse = userService.getUserDetails(userId);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
