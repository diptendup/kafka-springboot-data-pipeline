package com.mp.fintech.config;

import com.mp.fintech.constants.ApplicationConstants;
import com.mp.fintech.entity.Users;
import com.mp.fintech.exception.UserNotFoundException;
import com.mp.fintech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> userInfo = repository.findByName(username);
        return userInfo.map(UsersInfoDetails::new)
                .orElseThrow(() -> new UserNotFoundException(ApplicationConstants.USER_NOT_FOUND + username));

    }
}