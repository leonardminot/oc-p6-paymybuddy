package com.paymybuddy;

import com.paymybuddy.application.model.Relation;
import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.Transfer;
import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.application.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class PayMyBuddyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyAppApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner commandLineRunner(
            UserAccountRepositoryJpa userAccountRepositoryJpa,
            RelationRepositoryJpa relationRepositoryJpa,
            BalanceByCurrencyRepositoryJpa balanceByCurrencyRepositoryJpa,
            BankAccountRepositoryJpa bankAccountRepositoryJpa,
            BankTransactionRepositoryJpa bankTransactionRepositoryJpa,
            TransactionRepositoryJpa transactionRepositoryJpa,
            TransferRepositoryJpa transferRepositoryJpa) {
        return args -> {
            //testCreateUser(userAccountRepository, relationRepository);
//            UserAccount leo = new UserAccount(
//                    "Léo",
//                    "Minot",
//                    "leo.minot.pas.riche@email.com",
//                    "leo123",
//                    "PoPoZPasRiche"
//            );
            UserAccount leo = userAccountRepositoryJpa.findById(UUID.fromString("57ae3e38-3a0c-4675-98b0-d1627e4b4f09")).get();
            UserAccount victor = userAccountRepositoryJpa.findById(UUID.fromString("3f03d4ef-3cce-4583-80e4-b749802a09e3")).get();
//            System.out.println(leo);
//            BalanceByCurrency balanceByCurrencyLeoEuro = new BalanceByCurrency(
//                    1000.,
//                    "USD",
//                    leo
//            );
//            balanceByCurrencyRepository.save(balanceByCurrencyLeoEuro);
//            leo.getBalanceByCurrencyList().forEach(balanceByCurrency -> System.out.println(balanceByCurrency.getCurrency()));

//            BankAccount creditAgricoleLeo = new BankAccount("123456789", "FR", leo);
//            BankTransaction firstTransaction = new BankTransaction(100.0, "EUR", LocalDateTime.now(), creditAgricoleLeo);
//            BankTransaction secondTransaction = new BankTransaction(150.0, "EUR", LocalDateTime.now(), creditAgricoleLeo);

//            BankAccount creditAgricoleLeo = bankAccountRepository.findById(UUID.fromString("01b007dc-bf90-46d4-85bb-a0bb00d99597")).get();
//
//            Transaction transaction = new Transaction("Pour l'avenir", 100.0, "EUR", LocalDateTime.now());
//
//            transactionRepository.save(transaction);

            Transfer transfer = new Transfer(
                    leo,
                    victor,
                    new Transaction("Pour mettre du beurre dans les épinards", 50.0, "EUR", LocalDateTime.now())
            );

            transferRepositoryJpa.save(transfer);


//            creditAgricoleLeo.addTransaction(new BankTransaction(50.0, "EUR", LocalDateTime.now()));
//            bankAccountRepository.save(creditAgricoleLeo);

//            bankAccountRepository.save(creditAgricoleLeo);
//            bankTransactionRepository.save(firstTransaction);
//            bankTransactionRepository.save(secondTransaction);


        };
    }

    private void testCreateUser(UserAccountRepositoryJpa userAccountRepositoryJpa, RelationRepositoryJpa relationRepositoryJpa) {
        UserAccount leo = new UserAccount(
                "Léo",
                "Minot",
                "leo.minot@email.com",
                "leo123",
                "PoPoZ"
        );
        UserAccount victor = new UserAccount(
                "Victor",
                "Minot",
                "victor@email.com",
                "victor123",
                "miniPoPoZ"
        );
        userAccountRepositoryJpa.saveAll(List.of(leo, victor));
        Relation relationLeoVictor = new Relation(leo, victor, LocalDateTime.now());
        relationRepositoryJpa.save(relationLeoVictor);
    }

}
