package com.example.umc10th_kaito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@SpringBootApplication
@EnableJpaAuditing
public class Umc10thKaitoApplication {
    public static void main(String[] args) {
        SpringApplication.run(Umc10thKaitoApplication.class, args);
    }
}
