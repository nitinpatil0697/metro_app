package com.nitin.metro.model;

import lombok.Data;

@Data
public class GenerateTicketResponse {

    private String status;
    private String message;
    private Object result;
}
