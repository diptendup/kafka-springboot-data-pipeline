package com.mp.fintech.service;

import com.mp.fintech.entity.AuthorizationStatus;
import com.mp.fintech.repository.AuthorizationStatusRepository;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    @Autowired
    private AuthorizationStatusRepository repository;

    @Autowired
    private ObjectFactory<AuthorizationStatus> authorizationStatusFactory;

    public void addAuthLogEntry(String transactionId, String status, String reason) {

        AuthorizationStatus authorizationStatus = authorizationStatusFactory.getObject();
        authorizationStatus.setTransactionId(transactionId);
        authorizationStatus.setStatus(status);
        authorizationStatus.setReason(reason);

        repository.saveAndFlush(authorizationStatus);
    }
}
