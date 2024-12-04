package com.zxp.zxphomework;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestEncryptorTest {
    @Test
    public void test() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("saltkk");//yml文件里设置的key,对应的是盐值
        String urlEn = encryptor.encrypt("jdbc:mysql://localhost:3306/mscloud?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        String nameEn = encryptor.encrypt("2188965141@qq.com");
        String passwordEn = encryptor.encrypt("SDFWER1324657981");

        String urlDe = encryptor.decrypt(urlEn);
        String nameDe = encryptor.decrypt(nameEn);
        String passwordDe = encryptor.decrypt(passwordEn);
        System.out.println("url密文："+urlEn);
        System.out.println("url明文："+urlDe);
        System.out.println("username密文："+nameEn);
        System.out.println("username明文："+nameDe);
        System.out.println("password密文："+passwordEn);
        System.out.println("password明文："+passwordDe);
    }
}