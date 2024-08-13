package com.mp.fintech.util;

import com.mp.fintech.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class TransactionGenerator {

    private static final Logger log = LoggerFactory.getLogger(TransactionGenerator.class);

    private static final Random random = new Random();

    public static Transaction generateBulkTransaction() {

        Transaction transaction = new Transaction();
        String transactionId    = "TXN" + random.nextLong(100000000000000L);
        String creditCardNumber = "4321786534235656"; //generateCreditCardNumber(); Card number is configured for this account otherwise it throws an exception
                                                      // for a bulk operation. If need we need to store card numbers in the account_details table before validating/processing
        double amount = Math.round(random.nextDouble() * 50000);
        String merchantId = generateMerchant();

        transaction.setTransactionId(transactionId);
        transaction.setCardNumber(creditCardNumber);
        transaction.setAmount(amount);
        transaction.setMerchantId(merchantId);

        log.info("Transaction ID:" + transactionId + ", CreditCardNumber: " + creditCardNumber + ", Amount : " + amount + ", merchantId: " + merchantId);

        return transaction;
    }

    private static String generateCreditCardNumber() {
        StringBuilder creditCardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            creditCardNumber.append(random.nextInt(10));
        }
        return creditCardNumber.toString();
    }

    private static String generateMerchant() {
        StringBuilder merchantNumber = new StringBuilder("023");
        for (int i = 0; i < 6; i++) {
            merchantNumber.append(random.nextInt(10));
        }
        return merchantNumber.toString();
    }
}
