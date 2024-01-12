package com.paymybuddy.repository;

import com.paymybuddy.model.PayMyBuddyDeduction;
import com.paymybuddy.repository.definition.DeductionRepository;
import com.paymybuddy.repository.jpa.DeductionRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeductionRepositoryDB implements DeductionRepository {
    private final DeductionRepositoryJpa deductionRepositoryJpa;

    @Autowired
    public DeductionRepositoryDB(DeductionRepositoryJpa deductionRepositoryJpa) {
        this.deductionRepositoryJpa = deductionRepositoryJpa;
    }

    @Override
    public void save(PayMyBuddyDeduction deduction) {
        deductionRepositoryJpa.save(deduction);
    }

    @Override
    public List<PayMyBuddyDeduction> fetchAll() {
        return deductionRepositoryJpa.findAll();
    }
}
