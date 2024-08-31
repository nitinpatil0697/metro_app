package com.nitin.metro.api.request;

import com.nitin.metro.model.user.Address;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Address address;
}
