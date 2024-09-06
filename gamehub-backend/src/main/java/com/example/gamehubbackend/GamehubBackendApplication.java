package com.example.gamehubbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GamehubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamehubBackendApplication.class, args);
	}

}
