package com.nitin.metro.repository.user;

import com.nitin.metro.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEnabled(Boolean status);

    User findByEmail(String email);
}
