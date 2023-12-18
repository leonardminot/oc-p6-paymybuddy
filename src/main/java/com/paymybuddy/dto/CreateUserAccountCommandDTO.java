package com.paymybuddy.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateUserAccountCommandDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private String passwordConfirmation;
}
