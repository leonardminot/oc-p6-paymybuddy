package com.paymybuddy.domain.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DateProviderImpl implements DateProvider {
    @Override
    public LocalDateTime getNow() {
        return LocalDateTime.now();
    }
}
