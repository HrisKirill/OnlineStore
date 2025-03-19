package com.khrystoforov.onlinestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnlineStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineStoreApplication.class, args);
    }

}
