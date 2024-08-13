package com.mp.fintech.repository;

import com.mp.fintech.entity.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {

    Optional<AccountDetails> findByCardNumber(String cardNumber);
}
