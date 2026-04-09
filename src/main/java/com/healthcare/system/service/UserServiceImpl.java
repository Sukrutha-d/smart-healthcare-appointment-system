package com.healthcare.system.service;

import org.springframework.stereotype.Service;

@Service // 🔥 THIS IS THE MISSING PIECE
public class UserServiceImpl implements UserService {

    @Override
    public boolean validate(String username, String password) {
        // For now, let's use a hardcoded check. 
        // Later, you can add your Repository logic here to check the DB.
        if ("admin".equals(username) && "123".equals(password)) {
            return true;
        }
        return false;
    }
}