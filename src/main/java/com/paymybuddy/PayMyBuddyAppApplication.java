package com.paymybuddy;

import com.paymybuddy.model.BalanceByCurrency;
import com.paymybuddy.model.Relation;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.BalanceByCurrencyRepository;
import com.paymybuddy.repository.RelationRepository;
import com.paymybuddy.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
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
            BalanceByCurrencyRepository balanceByCurrencyRepository) {
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
//            System.out.println(leo);
//            BalanceByCurrency balanceByCurrencyLeoEuro = new BalanceByCurrency(
//                    1000.,
//                    "USD",
//                    leo
//            );
//            balanceByCurrencyRepository.save(balanceByCurrencyLeoEuro);
            leo.getBalanceByCurrencyList().forEach(balanceByCurrency -> System.out.println(balanceByCurrency.getCurrency()));
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
