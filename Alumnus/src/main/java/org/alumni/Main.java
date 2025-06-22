package org.alumni; // <-- The base package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // <-- This annotation starts the scan from 'org.alumni'
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}