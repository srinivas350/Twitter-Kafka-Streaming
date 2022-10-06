package com.example.kafkatwitterspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(basePackages="com.example")
@EnableWebMvc
public class KafkaTwitterSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaTwitterSpringbootApplication.class, args);
	}

}
