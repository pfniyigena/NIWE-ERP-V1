package com.niwe.erp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@Slf4j
public class NiweErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(NiweErpApplication.class, args);
	}

	@Bean
	CommandLineRunner test(ApplicationContext context) {
	    return args -> log.info("javaMailSender:{}", context.containsBean("javaMailSender"));
	}
	

}
