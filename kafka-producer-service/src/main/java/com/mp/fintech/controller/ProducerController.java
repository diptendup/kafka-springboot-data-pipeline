package com.mp.fintech.controller;

import com.mp.fintech.constants.ApplicationConstants;
import com.mp.fintech.entity.Transaction;
import com.mp.fintech.service.AuthorizationService;
import com.mp.fintech.service.KafkaMessageService;
import com.mp.fintech.util.TransactionGenerator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ProducerController {

    private static final Logger log = LoggerFactory.getLogger(ProducerController.class);

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private KafkaMessageService kafkaMessageService;

    private List<Transaction> transactionList = null;

    private ScheduledExecutorService scheduledService = null;

    @PostMapping("/publish")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<String> process(@Valid @RequestBody Transaction transaction, BindingResult result) {

        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(ApplicationConstants.COMMA_SEPARATED));
            log.error("Transaction validation errors: {}", errorMessage);

            addEntry(transaction, ApplicationConstants.DECLINED, errorMessage);

            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            kafkaMessageService.sendEventMessageToTopic(transaction);
            addEntry(transaction, ApplicationConstants.APPROVED, ApplicationConstants.SENT_SUCCESSFULLY);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApplicationConstants.SENT_MESSAGE);
    }

    @GetMapping("/publish/{bulkMessage}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> bulkTransactions(@PathVariable String bulkMessage) {
        scheduledService = Executors.newScheduledThreadPool(1);
        transactionList  = new ArrayList<>();
        try {
            startGeneratingBulkTransactions();
            addBulkEntry(bulkMessage);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApplicationConstants.BULK_MESSAGES);
    }

    private void addEntry(Transaction transaction, String status, String reason) {
        authorizationService.addAuthLogEntry(transaction.getTransactionId(), status, reason);
    }

    private void addBulkEntry(String message) {
        authorizationService.addAuthLogEntry(message, ApplicationConstants.APPROVED, ApplicationConstants.SENT_BULK_SUCCESSFULLY);
    }

    private void startGeneratingBulkTransactions() {
        scheduledService.scheduleAtFixedRate(() -> {
            if (transactionList.size() < ApplicationConstants.MAX_TRANSACTIONS) {
                Transaction transaction = TransactionGenerator.generateBulkTransaction();
                try {
                    kafkaMessageService.sendEventMessageToTopic(transaction);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                transactionList.add(transaction);
            } else {
                scheduledService.shutdown();
                log.info("Max transactions reached. Stopping generation.");
            }
        }, 0, 1, TimeUnit.SECONDS);

        try {
            // Wait for the scheduler to terminate
            while (!scheduledService.awaitTermination(1, TimeUnit.SECONDS)) { }
            log.info("All bulk transactions have been generated.");
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for tasks to finish.");
            scheduledService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
