package com.paymybuddy;

import com.paymybuddy.model.Relation;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.RelationRepository;
import com.paymybuddy.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class PayMyBuddyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserAccountRepository userAccountRepository, RelationRepository relationRepository) {
        return args -> {
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
        };
    }

}
