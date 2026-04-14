package com.healthcare.system.service;

import com.healthcare.system.model.User;

public interface UserService {

    User login(String username, String password);

}