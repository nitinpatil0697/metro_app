package com.nitin.metro.model.vendingMachine;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToMany
    private List<MetroStation> stations;
}
