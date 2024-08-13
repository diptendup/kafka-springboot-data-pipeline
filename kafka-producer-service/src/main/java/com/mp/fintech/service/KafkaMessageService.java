package com.mp.fintech.service;

import com.mp.fintech.constants.ApplicationConstants;
import com.mp.fintech.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessageService {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEventMessageToTopic(Transaction transaction) throws Exception{
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(ApplicationConstants.MP_MSG_TOPIC, transaction);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Message sent successfully {} with offset {}", transaction.toString(), result.getRecordMetadata().offset());
                } else {
                    log.error("Unable to send the message {} due to {}", transaction.toString(), ex.getMessage());
                }
            });

        } catch (Exception ex) {
            log.error("Exception occurs {} ", ex.getMessage());
            throw ex;
        }
    }
}
