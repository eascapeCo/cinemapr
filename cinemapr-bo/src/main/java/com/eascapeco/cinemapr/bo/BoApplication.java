package com.eascapeco.cinemapr.bo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackages = "com.eascapeco.cinemapr")
@EnableJpaRepositories(basePackages = "com.eascapeco.cinemapr")
@SpringBootApplication(scanBasePackages = "com.eascapeco.cinemapr")
public class BoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoApplication.class, args);
    }
}
