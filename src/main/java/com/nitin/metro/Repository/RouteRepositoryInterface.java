package com.nitin.metro.Repository;

import com.nitin.metro.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepositoryInterface extends JpaRepository<Route, Integer> {

}
