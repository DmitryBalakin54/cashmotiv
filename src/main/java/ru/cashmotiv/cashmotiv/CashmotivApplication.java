package ru.cashmotiv.cashmotiv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class CashmotivApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashmotivApplication.class, args);
	}

}
