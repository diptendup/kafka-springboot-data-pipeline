package com.mp.fintech.entity;

import com.mp.fintech.constants.ApplicationConstants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @NotNull(message = ApplicationConstants.TRANSACTIONID_CANNOT_NULL)
    private String transactionId;

    @NotEmpty
    @NotNull(message = ApplicationConstants.CARD_NUMBER_CANNOT_NULL)
    private String cardNumber;

    @NotNull(message = ApplicationConstants.AMOUNT_CANNOT_NULL)
    @Positive(message = "Amount must be positive")
    @Max(value = 50000, message = "Amount must not be greater than 50000")
    private Double amount;

    @NotEmpty
    @NotNull(message = ApplicationConstants.MERCHANTID_CANNOT_NULL)
    private String merchantId;
}
