package com.nitin.metro.service;

import com.nitin.metro.api.request.LoginRequest;
import com.nitin.metro.api.request.RegisterRequest;
import com.nitin.metro.api.response.GeneralResponse;
import com.nitin.metro.api.response.LoginResponse;
import com.nitin.metro.api.response.RegisterResponse;
import com.nitin.metro.model.user.User;
import com.nitin.metro.model.vendingMachine.TicketFare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.nitin.metro.constants.AppConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import com.nitin.metro.repository.user.UserRepository;

import static java.util.Objects.*;

@Service
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    /**
     * To Register User
     */
    public ResponseEntity<RegisterResponse> registerUser(RegisterRequest registerRequest) {
        LOGGER.info("API : registerUser called");
        RegisterResponse registerResponse = new RegisterResponse();
        try{
            if (!this.validateRegisterRequest(registerRequest)) {
                throw new Exception("VALIDATION_FAILED");
            }

            if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
                LOGGER.severe("Already registered with email.");
                throw new Exception("USER_ALREADY_REGISTERED");
            }

            User newUser = new User();
            LocalDateTime currentDateTime = LocalDateTime.now();
            newUser.setFirstName(registerRequest.getFirstName());
            newUser.setLastName(registerRequest.getLastName());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(registerRequest.getPassword());
            newUser.setCreatedAt(currentDateTime);
            newUser.setEnabled(true);
            newUser.setRole(AppConstants.ROLE_USER);
            newUser.setAddress(registerRequest.getAddress());
            newUser.setPhone(registerRequest.getPhone());
            userRepository.save(newUser);

            registerResponse.setStatus(AppConstants.SUCCESS);
            registerResponse.setMessage("User registered successfully");
            LOGGER.info("User registered successfully");
        } catch (Exception e){
            LOGGER.severe("registerUser : Exception : " + e.getMessage());
            registerResponse.setStatus(AppConstants.FAILURE);
            registerResponse.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    /**
     * To Validate Register request
     */
    public boolean validateRegisterRequest(RegisterRequest registerRequest) {
        if (isNull(registerRequest.getFirstName()) || isNull(registerRequest.getLastName()) ||
            isNull(registerRequest.getEmail()) || isNull(registerRequest.getPassword())
          ){
            return false;
        }
        LOGGER.info("Validated register request successfully.");
        return true;
    }

    /**
     * To get All Users
     */
    public ResponseEntity<List<User>> getAllUsers() {
        LOGGER.info("API : getAllUsers called");
        List<User> users = userRepository.findAll();
        LOGGER.info("API : getAllUsers fetched successfully");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * To get Enabled users
     */
    public ResponseEntity<List<User>> getEnabledUsers() {
        LOGGER.info("API : getEnabledUsers called");
        List<User> users = userRepository.findByEnabled(true);
        LOGGER.info("API : getEnabledUsers fetched successfully");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Get Email by profile
     */
    public User getByEmailByProfile(String email) {
        LOGGER.info("API : getEnabledUsers called");
        User user = userRepository.findByEmail(email);
        LOGGER.info("API : getEnabledUsers fetched successfully");
        return user;
    }

    /**
     * To log in with user credentials
     */
    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) {
        User userData = userRepository.findByEmail(loginRequest.getEmail());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatus(AppConstants.FAILURE);
        if (userData == null) {
            LOGGER.info("No user found with email : " + loginRequest.getEmail());
            loginResponse.setMessage("No user found with email ");
        } else {
            if (!userData.getPassword().equals(loginRequest.getPassword())) {
                LOGGER.info("Invalid password");
                loginResponse.setMessage("Invalid password");
            } else {
                loginResponse.setStatus(AppConstants.SUCCESS);
                loginResponse.setMessage("User logged in successfully");
                HashMap<String, String> result = new HashMap<>();
                final String jwt = jwtUtil.generateToken(userData);
                result.put("token", jwt);
                result.put("name", userData.getFirstName());
                result.put("userEmail", userData.getEmail());
                result.put("role", userData.getRole());
                loginResponse.setResult(result);
            }
        }
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public ResponseEntity<GeneralResponse> updateUserDetails(Long userId , User userdata) {
        GeneralResponse response = new GeneralResponse();
        Optional<User> existingUsers = userRepository.findById(userId);
        if (existingUsers.isPresent()) {
            User existingUser = existingUsers.get();
            existingUser.setFirstName(userdata.getFirstName());
            existingUser.setLastName(userdata.getLastName());
            existingUser.setEmail(userdata.getEmail());
            existingUser.setRole(userdata.getRole());
            existingUser.setPhone(userdata.getPhone());
            existingUser.setEnabled(userdata.getEnabled());
            userRepository.save(existingUser);
        }
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Updated Data successfully");
        response.setResult(userdata);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
