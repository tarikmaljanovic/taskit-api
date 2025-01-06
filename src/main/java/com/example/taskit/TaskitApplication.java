package com.example.taskit;

import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

// http://localhost:8080/swagger-ui/index.html
public class TaskitApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskitApplication.class, args);
    }
}
