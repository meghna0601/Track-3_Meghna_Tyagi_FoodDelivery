package com.user.controller;

import com.user.dto.UserRequest;
import com.user.dto.UserResponse;
import com.user.dto.UserUpdateResponse;
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
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

//    @DeleteMapping
//    public ResponseEntity<UserResponse> deleteUser(@RequestBody UserRequest userRequest) {
//
//    }
//
//    @PutMapping("/role")
//    public ResponseEntity<UserResponse> updateRole(@RequestBody UserRequest userRequest) {
//
//    }
//
//    @GetMapping("{userId}")
//    public ResponseEntity<UserResponse> getUser(@RequestParam String userId) {

//    }
}
