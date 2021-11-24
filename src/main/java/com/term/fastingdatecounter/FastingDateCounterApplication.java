package com.term.fastingdatecounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FastingDateCounterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastingDateCounterApplication.class, args);
	}

}
