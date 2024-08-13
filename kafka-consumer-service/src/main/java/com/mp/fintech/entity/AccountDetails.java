package com.mp.fintech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account_details")
public class AccountDetails {

    @Id
    private int id;
    private String cardNumber;
    private Double creditLimit;
    private String cvv;
    private LocalDateTime expirationDate;
}
