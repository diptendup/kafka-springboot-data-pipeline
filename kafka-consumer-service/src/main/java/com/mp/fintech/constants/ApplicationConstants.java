package com.mp.fintech.constants;

public class ApplicationConstants {

    private ApplicationConstants() {}

    public static final int CARD_LENGTH                  = 16;
    public static final int SET_CONCURRENCY_LEVEL        = 3;
    public static final int FIXED_THREAD_POOLS           = 4;
    public static final String APPLICATION_RUNNING       = "Application is up and running!";

    public static final String MAIN_TOPIC                = "maintopic";
    public static final String DLQ_TOPIC                 = "dlqtopic";
    public static final String MP_MSG_TOPIC              = "mp-mgs-topic";
    public static final String MP_MSG_GROUP              = "mp-mgs-group";
    public static final String MP_MSG_GROUP_DLQ          = "mp-mgs-group-DLQ";
    public static final String MP_MSG_TOPIC_DLQ          = "mp-mgs-topic-DLQ";

    public static final String REJECTED                  = "Rejected";
    public static final String RECEIVED                  = "Received";

    public static final String CARD_NUMBER_LENGTH        = "Card number must be a 16 digits";
    public static final String CARD_NOT_FOUND            = "Card number not found";
    public static final String RECEIVED_SUCCESSFULLY     = "Consumer - Processed Successfully!";

    public static final String CARD_NUMBER               = "CardNumber";
    public static final String TRANSACTION_AMT           = "Transaction amount ";
    public static final String EXCEEDS_MAX_AMT           = "exceeds the maximum allowed limit";
    public static final String VALIDATION_FAILED         = "Validation failed for this transaction";
}
