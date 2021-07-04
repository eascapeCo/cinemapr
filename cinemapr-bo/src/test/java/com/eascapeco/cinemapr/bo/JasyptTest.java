package com.eascapeco.cinemapr.bo;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JasyptTest {
    @Test
    void jasypt_encrypt_decrypt_test() {

        String dbUrl = "jdbc:mariadb://dm1623242781256.fun25.co.kr:12002/cinemaprdev?characterEncoding=UTF-8&serverTimezone=UTC";
        String dbUser = "tester";
        String dbPw = "";

        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword("");
        jasypt.setAlgorithm("PBEWithMD5AndDES");

        String encryptedDbUrl = jasypt.encrypt(dbUrl);
        String decryptedDbUrl = jasypt.decrypt(encryptedDbUrl);

        String encryptedDbUser = jasypt.encrypt(dbUser);
        String decryptedDbUser = jasypt.decrypt(encryptedDbUser);

        String encryptedDbPw = jasypt.encrypt(dbPw);
        String decryptedDbPw = jasypt.decrypt(encryptedDbPw);

        assertThat(dbUrl).isEqualTo(decryptedDbUrl);
        assertThat(dbUser).isEqualTo(decryptedDbUser);
        assertThat(dbPw).isEqualTo(decryptedDbPw);


        System.out.println(dbUrl + " -> " + encryptedDbUrl);
        System.out.println(dbUser + " -> " + encryptedDbUser);
        System.out.println(dbPw + " -> " + encryptedDbPw);

        System.out.println(encryptedDbUrl);
        System.out.println(encryptedDbUser);
        System.out.println(encryptedDbPw);
    }
}
