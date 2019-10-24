package com.guerrero.app.ws;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MobileAppWsApplication {

	final private static Logger logger = Logger.getLogger(MobileAppWsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {

		logger.info("MobileAppWsApplication.bCryptPasswordEncoder");
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}
}
