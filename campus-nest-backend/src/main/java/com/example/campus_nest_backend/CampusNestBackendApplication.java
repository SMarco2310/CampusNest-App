package com.example.campus_nest_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CampusNestBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusNestBackendApplication.class, args);
	}
}
