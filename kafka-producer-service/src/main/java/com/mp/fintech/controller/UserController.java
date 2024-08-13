package com.mp.fintech.controller;

import com.mp.fintech.constants.ApplicationConstants;
import com.mp.fintech.dto.AuthenticationRequest;
import com.mp.fintech.entity.Users;
import com.mp.fintech.service.TokenService;
import com.mp.fintech.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String getMessage() {
        return ApplicationConstants.APPLICATION_RUNNING;
    }

    @PostMapping("/user")
    public ResponseEntity<String> addNewUser(@Valid @RequestBody Users user, BindingResult result) {

        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(ApplicationConstants.COMMA_SEPARATED));
            log.error("User validation errors: {}", errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }else if (service.isUserPresent(user.getName())) {
             return ResponseEntity.status(HttpStatus.CONFLICT).body(ApplicationConstants.USER_ALREADY_EXISTS);
        }else {
             service.addUser(user);
             return ResponseEntity.status(HttpStatus.CREATED).body(ApplicationConstants.ADDED_NEW_USER);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<Users> getUser(@PathVariable String username) {
         Users user = service.getUserByName(username);
         return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Users> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping("/token")
    public String generateTokenOnceUserAuthenticated(@RequestBody AuthenticationRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        String token = null;
        if (authentication.isAuthenticated()) {
            token = tokenService.generateToken(authRequest.getUsername());
        }
        return token;
    }

}
