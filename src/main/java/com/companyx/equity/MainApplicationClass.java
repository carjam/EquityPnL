package com.companyx.equity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// right click on pom.xml --> Maven --> download sources
// Maven app -- right click on pom.xml --> Maven --> Generate Sources  ...
// build project
// right click on this class and choose Run
// http://localhost:8080/Widget/1

@SpringBootApplication
public class MainApplicationClass {

    public static void main(String[] args) {
        SpringApplication.run(MainApplicationClass.class, args);
    }
}