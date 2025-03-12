package org.example.prm392_groupprojectbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Prm392GroupProjectBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Prm392GroupProjectBeApplication.class, args);
    }

}
