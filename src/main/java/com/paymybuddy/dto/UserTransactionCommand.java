package com.paymybuddy.dto;

import com.paymybuddy.model.UserAccount;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserTransactionCommand {
    private UserAccount fromUser;
    private UserAccount toUser;
    private String description;
    private String currency;
    private double amount;
}
