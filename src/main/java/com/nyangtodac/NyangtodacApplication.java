package com.nyangtodac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NyangtodacApplication {

	public static void main(String[] args) {
		SpringApplication.run(NyangtodacApplication.class, args);
	}

}
