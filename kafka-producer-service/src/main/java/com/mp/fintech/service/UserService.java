package com.mp.fintech.service;

import com.mp.fintech.constants.ApplicationConstants;
import com.mp.fintech.entity.Users;
import com.mp.fintech.exception.UserNotFoundException;
import com.mp.fintech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    public boolean isUserPresent(String username) {
        Optional<Users> userInfo = repository.findByName(username);
        return userInfo.isPresent();
    }

    public Users getUserByName(String username) {
        return repository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException(ApplicationConstants.USER_NOT_FOUND + username));
    }

    public List<Users> getAllUsers() {
        return repository.findAll();
    }

}
