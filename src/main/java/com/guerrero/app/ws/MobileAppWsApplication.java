package com.guerrero.app.ws;

import com.guerrero.app.ws.security.AppProperties;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class MobileAppWsApplication extends SpringBootServletInitializer {

	final private static Logger logger = Logger.getLogger(MobileAppWsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}


	/**
	 * permite compilar la aplicacion como war
	 * @param builder
	 * @return
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MobileAppWsApplication.class);
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
