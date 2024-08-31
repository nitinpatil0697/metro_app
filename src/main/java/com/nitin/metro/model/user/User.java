package com.nitin.metro.model.user;

import jakarta.persistence.*;
import lombok.Data;
import com.nitin.metro.model.user.Address;

import java.time.LocalDateTime;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
    private Boolean enabled;
    @Embedded
    private Address address;
}
