package com.niwe.erp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NiweErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(NiweErpApplication.class, args);
	}

	@Bean
	public CommandLineRunner test(ApplicationContext context) {
	    return args -> System.out.println(context.containsBean("javaMailSender"));
	}
	

}
