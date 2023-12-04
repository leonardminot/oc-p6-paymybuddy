package com.paymybuddy.domain.model;

import java.time.LocalDateTime;

public record UserRelationModel(
        UserAccountModel user1,
        UserAccountModel user2,
        LocalDateTime createdAt
) {
}
