package com.paymybuddy.coremodel.model;

import java.util.UUID;

public record UserAccountModel(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String password,
        String username
) {
}
