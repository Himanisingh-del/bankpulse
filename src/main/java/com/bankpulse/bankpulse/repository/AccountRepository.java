package com.bankpulse.bankpulse.repository;

import com.bankpulse.bankpulse.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    long countByBalanceGreaterThan(BigDecimal balance);
}