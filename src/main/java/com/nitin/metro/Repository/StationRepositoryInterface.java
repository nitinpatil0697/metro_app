package com.nitin.metro.Repository;

import com.nitin.metro.model.MetroStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepositoryInterface extends JpaRepository<MetroStation, Integer> {

}
