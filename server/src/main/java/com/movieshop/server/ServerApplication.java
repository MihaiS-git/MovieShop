package com.movieshop.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
		System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
		System.setProperty("POSTGRES_DB", dotenv.get("POSTGRES_DB"));
		System.setProperty("SPRING_SECURITY_USER_NAME", dotenv.get("SPRING_SECURITY_USER_NAME"));
		System.setProperty("SPRING_SECURITY_USER_PASSWORD", dotenv.get("SPRING_SECURITY_USER_PASSWORD"));
		System.setProperty("PGADMIN_EMAIL", dotenv.get("PGADMIN_EMAIL"));
		System.setProperty("PGADMIN_PASSWORD", dotenv.get("PGADMIN_PASSWORD"));
		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
		System.setProperty("BASE_URL", dotenv.get("BASE_URL"));
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));

		SpringApplication.run(ServerApplication.class, args);
	}

}
