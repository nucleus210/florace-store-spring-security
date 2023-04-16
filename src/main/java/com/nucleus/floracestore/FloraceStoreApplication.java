package com.nucleus.floracestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.nucleus.floracestore")
public class FloraceStoreApplication {
    public static void main(String[] args) {

        SpringApplication.run(FloraceStoreApplication.class, args);

    }

}

