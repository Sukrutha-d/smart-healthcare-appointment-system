package com.healthcare.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.healthcare.system.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ CHANGE THIS
    User findByEmail(String email);

}