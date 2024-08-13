package com.mp.fintech.controller;

import com.mp.fintech.constants.ApplicationConstants;
import com.mp.fintech.entity.Transaction;
import com.mp.fintech.service.AuthorizationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ConsumerController {

    private static final Logger log = LoggerFactory.getLogger(ConsumerController.class);

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/welcome")
    public String getMessage() {
        return ApplicationConstants.APPLICATION_RUNNING;
    }
    @PostMapping("/publish")
    public ResponseEntity<String> process(@Valid @RequestBody Transaction transaction, BindingResult result) {

        return ResponseEntity.ok("");

    }

}
