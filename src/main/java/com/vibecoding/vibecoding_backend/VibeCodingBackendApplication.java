package com.vibecoding.vibecoding_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * VibeCode Backend Application
 * 
 * @author VibeCode Team
 */
@SpringBootApplication
@MapperScan("com.vibecoding.vibecoding_backend.mapper")
public class VibeCodingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VibeCodingBackendApplication.class, args);
	}

}
