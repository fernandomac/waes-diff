package com.waes.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.waes.web.config.ApplicationConfig;


@SpringBootApplication(scanBasePackageClasses = { ApplicationConfig.class })
public class WebDiffApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebDiffApplication.class);
	
	public static void main(String[] args) throws InterruptedException {
		LOGGER.info("Starting WAES Web Diff Application");
		SpringApplication.run(WebDiffApplication.class, args);
	}
}
