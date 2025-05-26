package com.movieshop.loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class LoaderApplication {

	public static void main(String[] args) {
		System.out.println("==> LoaderApplication.run() called with args: " + Arrays.toString(args));

		SpringApplication.run(LoaderApplication.class, args);
	}

}
