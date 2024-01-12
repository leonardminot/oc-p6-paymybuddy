package com.paymybuddy.repository.definition;

import com.paymybuddy.model.PayMyBuddyDeduction;

import java.util.List;

public interface DeductionRepository {
    void save(PayMyBuddyDeduction deduction);
    List<PayMyBuddyDeduction> fetchAll();
}
