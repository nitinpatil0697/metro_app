package com.nitin.metro.repository.vendingMachine;

import com.nitin.metro.model.vendingMachine.MetroStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepositoryInterface extends JpaRepository<MetroStation, Integer> {

}
