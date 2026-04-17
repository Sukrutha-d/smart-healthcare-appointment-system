package com.healthcare.system.factory;

import com.healthcare.system.enums.AccountStatus;
import com.healthcare.system.enums.Role;
import com.healthcare.system.model.*;

import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public User createUser(Role role, String name, String email, String password, String extraData) {
        User user;
        switch (role) {
            case DOCTOR:
                Doctor doc = new Doctor();
                doc.setSpecialization(extraData);
                user = doc;
                break;
            case STAFF:
                Staff staff = new Staff();
                staff.setDepartment(extraData);
                user = staff;
                break;
            case ADMIN:
                user = new Admin();
                break;
            case PATIENT:
            default:
                user = new Patient();
                break;
        }
        
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); 
        user.setRole(role);
        user.setStatus(AccountStatus.ACTIVE);
        
        return user;
    }
}
