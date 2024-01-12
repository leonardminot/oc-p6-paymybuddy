package com.paymybuddy.utils;

import com.paymybuddy.model.PayMyBuddyDeduction;
import com.paymybuddy.repository.definition.DeductionRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeDeductionRepository implements DeductionRepository {
    List<PayMyBuddyDeduction> deductions = new ArrayList<>();
    @Override
    public void save(PayMyBuddyDeduction deduction) {
        deductions.add(deduction);
    }

    @Override
    public List<PayMyBuddyDeduction> fetchAll() {
        return deductions;
    }
}
