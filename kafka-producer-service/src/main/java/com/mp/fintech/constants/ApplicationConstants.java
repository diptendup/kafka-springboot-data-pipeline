package com.mp.fintech.constants;

public class ApplicationConstants {

    private ApplicationConstants() {}

    public static final int    SEVEN                = 7;

    public static final String COMMA_SEPARATED      = ", ";

    public static final String AUTHORIZATION        = "Authorization";
    public static final String BEARER               = "Bearer ";

    public static final String APPLICATION_RUNNING  = "Application is up and running!";
    public static final String USER_NOT_FOUND       = "User not found with name: ";
    public static final String USER_ALREADY_EXISTS  = "User is already exists!";
    public static final String ADDED_NEW_USER       = "User is successfully added!";

    public static final String NAME_CANNOT_NULL     = "Name cannot be null";
    public static final String PWD_CANNOT_NULL      = "Password cannot be null";
    public static final String EMAIL_CANNOT_NULL    = "Email cannot be null";
    public static final String ROLE_CANNOT_NULL     = "Roles cannot be null";


    public static final String TRANSACTIONID_CANNOT_NULL = "Transaction id cannot be null";
    public static final String CARD_NUMBER_CANNOT_NULL   = "Card number cannot be null";
    public static final String AMOUNT_CANNOT_NULL        = "Amount cannot be null";
    public static final String MERCHANTID_CANNOT_NULL    = "Merchant id cannot be null";

    public static final String DECLINED                  = "Declined";
    public static final String APPROVED                  = "Approved";
    public static final String SENT_SUCCESSFULLY         = "Producer - Successfully sent to the consumer!";
    public static final String SENT_BULK_SUCCESSFULLY    = "Producer - Successfully sent bulk messages to the consumer!";

    //Kafka
    public static final String MP_MSG_TOPIC              = "mp-mgs-topic";
    public static final String MP_MSG_TOPIC_DLQ          = "mp-mgs-topic-DLQ";
    public static final String SENT_MESSAGE              = "Successfully sent!";
    public static final String BULK_MESSAGES             = "Successfully sent bulk messages!";

    public static final int MAX_TRANSACTIONS             = 10;

    public static final Long   FIFTEEN_MINS              =  1000L * 60 * 15; //900,000 milliseconds
    public static final String SECRET_KEY                = "7E2F1D8A9B0C6D4E5F7A1B2C3D4E5F6A7B8C9D0E1F2A3B4C5D6E7F8A9B0C1D2E";
}
