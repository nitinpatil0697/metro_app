package com.nitin.metro.model.user;

import jakarta.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class Address {
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postal_code;
    private String country;
}
