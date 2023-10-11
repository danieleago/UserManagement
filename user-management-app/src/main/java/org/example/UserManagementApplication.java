package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementApplication {
	private static final Logger log = LoggerFactory.getLogger(UserManagementApplication.class);
	public static void main(String[] args) {
		log.debug("start main");
		SpringApplication.run(UserManagementApplication.class, args);
	}

}
