package com.zxp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class ZxpApplication{

    public static void main(String[] args) {
        SpringApplication.run(ZxpApplication.class, args);
    }

}


