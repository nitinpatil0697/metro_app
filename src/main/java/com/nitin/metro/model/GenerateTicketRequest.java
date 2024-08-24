package com.nitin.metro.model;

import lombok.Data;

@Data
public class GenerateTicketRequest {

    private String userName;
    private String routeName;
    private String ticketType;
    private Boolean peakHour;
}
