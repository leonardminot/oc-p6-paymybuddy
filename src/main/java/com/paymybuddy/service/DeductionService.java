package com.paymybuddy.service;

import com.paymybuddy.model.PayMyBuddyDeduction;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.definition.DeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeductionService {
    private final DeductionRepository deductionRepository;
    private final Double DEDUCTION_RATIO = 0.995;

    @Autowired
    public DeductionService(DeductionRepository deductionRepository) {
        this.deductionRepository = deductionRepository;
    }

    public Double getDeductionAmount(Double transactionAmount) {
        return (double) Math.round((transactionAmount * (1 - DEDUCTION_RATIO)) * 1000) / 1000;
    }

    public void saveDeductionOfTransaction(Transaction transaction) {
        deductionRepository.save(new PayMyBuddyDeduction(transaction, getDeductionAmount(transaction.getAmount() / 0.995 )));
    }
}
