package com.paymybuddy.domain.service;

import java.time.LocalDateTime;

public interface DateProvider {
    LocalDateTime getNow();
}
