package com.paymybuddy.dto;

import com.paymybuddy.model.BankAccount;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BankTransactionCommandDTO{
    private BankAccount bankAccount;
    private double amount;
    private String currency;
}
