package com.nitin.metro.service;

import com.nitin.metro.api.request.RegisterRequest;
import com.nitin.metro.api.response.RegisterResponse;
import com.nitin.metro.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.nitin.metro.constants.AppConstants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import com.nitin.metro.Repository.user.UserRepository;

import static java.util.Objects.*;

@Service
public class UserService {
    private static final Logger LOGGER = Logger.getLogger(VendingMachineService.class.getName());

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<RegisterResponse> registerUser(RegisterRequest registerRequest) {
        LOGGER.info("API : registerUser called");
        RegisterResponse registerResponse = new RegisterResponse();
        try{
            if (!this.validateRegisterRequest(registerRequest)) {
                throw new Exception("VALIDATION_FAILED");
            }

            LocalDateTime currentDateTime = LocalDateTime.now();
            User newUser = new User();
            newUser.setFirstName(registerRequest.getFirstName());
            newUser.setLastName(registerRequest.getLastName());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(registerRequest.getPassword());
            newUser.setCreatedAt(currentDateTime);
            newUser.setEnabled(true);
            newUser.setRole(AppConstants.ROLE_USER);
            userRepository.save(newUser);
            registerResponse.setStatus(AppConstants.SUCCESS);
            registerResponse.setMessage("User registered successfully");
        } catch (Exception e){
            LOGGER.severe("registerUser : Exception : " + e.getMessage());
            registerResponse.setStatus(AppConstants.FAILURE);
            registerResponse.setMessage(e.getMessage());
        }
        LOGGER.info("User registered successfully");
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    public boolean validateRegisterRequest(RegisterRequest registerRequest) {
        if (isNull(registerRequest.getFirstName()) || isNull(registerRequest.getLastName()) ||
            isNull(registerRequest.getEmail()) || isNull(registerRequest.getPassword())
          ){
            return false;
        }
        LOGGER.info("Validated register request.");
        return true;
    }

    public ResponseEntity<List<User>> getAllUsers() {
        LOGGER.info("API : getAllUsers called");
        List<User> users = userRepository.findAll();
        LOGGER.info("API : getAllUsers fetched successfully");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public ResponseEntity<List<User>> getEnabledUsers() {
        LOGGER.info("API : getEnabledUsers called");
        List<User> users = userRepository.findByEnabled(true);
        LOGGER.info("API : getEnabledUsers fetched successfully");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public ResponseEntity<User> getByEmailByProfile(String email) {
        LOGGER.info("API : getEnabledUsers called");
        User user = userRepository.findByEmail(email);
        LOGGER.info("API : getEnabledUsers fetched successfully");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
