package com.nitin.metro.controller;

import com.nitin.metro.api.request.RegisterRequest;
import com.nitin.metro.api.response.RegisterResponse;
import com.nitin.metro.model.user.User;
import com.nitin.metro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest){
        return userService.registerUser(registerRequest);
    }

    @GetMapping("allUsers")
    public ResponseEntity<List<User>> allRegisteredUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("allUsers/enabled")
    public ResponseEntity<List<User>> allEnabledUsers() {
        return userService.getEnabledUsers();
    }

    @GetMapping("profile/{email}")
    public ResponseEntity<User> getUserProfile(@PathVariable String email) {
        return userService.getByEmailByProfile(email);
    }
    
}
