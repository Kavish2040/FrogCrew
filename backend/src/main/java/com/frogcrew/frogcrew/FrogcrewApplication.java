package com.frogcrew.frogcrew;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class FrogcrewApplication {
	@Bean
	public CommandLineRunner runner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Loaded Beans:");
			Arrays.stream(ctx.getBeanDefinitionNames()).sorted().forEach(System.out::println);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(FrogcrewApplication.class, args);
	}

}
