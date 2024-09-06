package com.nitin.metro.Repository.vendingMachine;

import com.nitin.metro.model.vendingMachine.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepositoryInterface extends JpaRepository<Route, Integer> {

    List<Route> findByName(String routeName);
}
