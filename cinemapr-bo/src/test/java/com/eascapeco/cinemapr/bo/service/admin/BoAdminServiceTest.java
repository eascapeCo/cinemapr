package com.eascapeco.cinemapr.bo.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest(
    properties = {
        "spring.profiles.active=local",
        "jasypt.encryptor.password=cinemaPr2021!@"
    }
)
class BoAdminServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void createAdmin() {
        String password = "1234";
        String encode = passwordEncoder.encode(password);
        System.out.println(encode);
        assertThat(password).isEqualTo(passwordEncoder.matches(password, encode));
    }
}