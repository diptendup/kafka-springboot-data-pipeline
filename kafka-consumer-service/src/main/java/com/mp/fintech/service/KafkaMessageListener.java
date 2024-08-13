package com.mp.fintech.service;

import com.mp.fintech.constants.ApplicationConstants;
import com.mp.fintech.entity.AccountDetails;
import com.mp.fintech.entity.Transaction;
import com.mp.fintech.repository.AccountDetailsRepository;
import com.mp.fintech.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KafkaMessageListener {

    Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(ApplicationConstants.FIXED_THREAD_POOLS);

    @KafkaListener(topics = ApplicationConstants.MP_MSG_TOPIC, groupId = ApplicationConstants.MP_MSG_GROUP,
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionEvents(Transaction transaction) {

        executorService.submit( () ->  {
            process(transaction, ApplicationConstants.MAIN_TOPIC);
        });
        log.info("main consumer consume the events {} ", transaction.toString());
    }

    @KafkaListener(topics = ApplicationConstants.MP_MSG_TOPIC_DLQ, groupId = ApplicationConstants.MP_MSG_GROUP_DLQ,
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeRetryTransactionEvents(Transaction transaction) {

        executorService.submit( () ->  {
            process(transaction, ApplicationConstants.DLQ_TOPIC);
        });
        log.info("Retry consumer consume the events {} ", transaction.toString());
    }

    private void process(Transaction transaction, String readFromWhichTopic) {
        log.info("Consuming and Processing the transaction");
        try {
            if (!isAuthorizeTransaction(transaction, readFromWhichTopic)) {
                log.error(ApplicationConstants.VALIDATION_FAILED + " {}", transaction.getTransactionId());
            } else {
                transactionRepository.save(transaction);
                addEntry(transaction, ApplicationConstants.RECEIVED, ApplicationConstants.RECEIVED_SUCCESSFULLY);
            }
        }catch (Exception ex){
            log.error("Due to exception, transaction is moved to the DLQ topic {}", ex.getMessage());
        }
    }

    private boolean isAuthorizeTransaction(Transaction transaction, String readFromWhichTopic) {

        if (transaction.getCardNumber().length() < ApplicationConstants.CARD_LENGTH) {
            log.info("{} {} ", ApplicationConstants.CARD_NUMBER_LENGTH, transaction.getCardNumber());
            addEntry(transaction, ApplicationConstants.REJECTED, ApplicationConstants.CARD_NUMBER_LENGTH);
            return false;
        }

        Optional<AccountDetails> accountDetailsOptional = accountDetailsRepository.findByCardNumber(transaction.getCardNumber());

        if (accountDetailsOptional.isEmpty()) {
            log.info("{} {} ", ApplicationConstants.CARD_NOT_FOUND, transaction.getCardNumber());
            addEntry(transaction, ApplicationConstants.REJECTED, ApplicationConstants.CARD_NOT_FOUND);
            return false;
        }

        AccountDetails accountDetails = accountDetailsOptional.get();

        if (transaction.getAmount() > accountDetails.getCreditLimit() ) {
            log.info(ApplicationConstants.CARD_NUMBER + " {} " + ApplicationConstants.TRANSACTION_AMT + " {} " + ApplicationConstants.EXCEEDS_MAX_AMT + " {} ", transaction.getCardNumber(), transaction.getAmount(), accountDetails.getCreditLimit());
            addEntry(transaction, ApplicationConstants.REJECTED, ApplicationConstants.EXCEEDS_MAX_AMT);
            return false;
        }

        return true;
    }

    private void addEntry(Transaction transaction, String status, String reason) {
        authorizationService.addAuthLogEntry(transaction.getTransactionId(), status, reason);
    }
}
