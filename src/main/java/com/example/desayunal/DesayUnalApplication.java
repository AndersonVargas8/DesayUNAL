package com.example.desayunal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

public class    DesayUnalApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesayUnalApplication.class, args);
    }

}
