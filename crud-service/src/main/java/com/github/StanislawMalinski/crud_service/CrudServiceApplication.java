package com.github.stanislawmalinski.crud_service;

import com.github.stanislawmalinski.crud_service.models.Role;
import com.github.stanislawmalinski.crud_service.models.User;
import com.github.stanislawmalinski.crud_service.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class CrudServiceApplication {
    static Logger log = LoggerFactory.getLogger(CrudServiceApplication.class);

	public static void main(String[] args) {
        log.info("The Crud service has been started. Check out my swegger: {}", "http://localhost:8080/swagger-ui/index.html");
		SpringApplication.run(CrudServiceApplication.class, args);
	}
}
