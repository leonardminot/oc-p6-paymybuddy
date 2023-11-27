package com.paymybuddy;

import com.paymybuddy.model.*;
import com.paymybuddy.repository.*;
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
            UserAccountRepository userAccountRepository,
            RelationRepository relationRepository,
            BalanceByCurrencyRepository balanceByCurrencyRepository,
            BankAccountRepository bankAccountRepository,
            BankTransactionRepository bankTransactionRepository,
            TransactionRepository transactionRepository,
            TransferRepository transferRepository) {
        return args -> {
            //testCreateUser(userAccountRepository, relationRepository);
//            UserAccount leo = new UserAccount(
//                    "Léo",
//                    "Minot",
//                    "leo.minot.pas.riche@email.com",
//                    "leo123",
//                    "PoPoZPasRiche"
//            );
            UserAccount leo = userAccountRepository.findById(UUID.fromString("57ae3e38-3a0c-4675-98b0-d1627e4b4f09")).get();
            UserAccount victor = userAccountRepository.findById(UUID.fromString("3f03d4ef-3cce-4583-80e4-b749802a09e3")).get();
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

            transferRepository.save(transfer);


//            creditAgricoleLeo.addTransaction(new BankTransaction(50.0, "EUR", LocalDateTime.now()));
//            bankAccountRepository.save(creditAgricoleLeo);

//            bankAccountRepository.save(creditAgricoleLeo);
//            bankTransactionRepository.save(firstTransaction);
//            bankTransactionRepository.save(secondTransaction);


        };
    }

    private void testCreateUser(UserAccountRepository userAccountRepository, RelationRepository relationRepository) {
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
        userAccountRepository.saveAll(List.of(leo, victor));
        Relation relationLeoVictor = new Relation(leo, victor, LocalDateTime.now());
        relationRepository.save(relationLeoVictor);
    }

}
