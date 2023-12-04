package com.paymybuddy.domain.dto;

public record UserRequestCommandDTO(
        String username,
        String email,
        String password,
        String firstName,
        String lastName
) {
}
