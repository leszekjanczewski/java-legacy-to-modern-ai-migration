package com.legacy.creditscoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// LEGACY: Prosta klasa startowa bez jawnej konfiguracji
// Boot 1.5 auto-configuration "magicznie" konfiguruje wszystko:
// JPA, Hibernate, DataSource, H2 console, Jackson, Tomcat
// Deweloperzy legacy często nie rozumieli co dokładnie jest konfigurowane
// i dlaczego - co utrudniało debugowanie problemów produkcyjnych.
//
// Brak:
// - @EnableTransactionManagement (auto-config to ogarnia)
// - @EnableJpaRepositories (auto-config to ogarnia)
// - Jawnej konfiguracji DataSource jako @Bean
// - Profili Spring (@Profile("dev"), @Profile("prod"))
@SpringBootApplication
public class CreditScoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditScoringApplication.class, args);
    }
}
