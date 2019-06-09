package com.waes.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(value={
	"com.waes.repository",
	"com.waes.service",
	"com.waes.web"})
public class WebDiffApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebDiffApplication.class);
	
	public static void main(String[] args) throws InterruptedException {
		LOGGER.info("Starting WAES Web Diff Application");
		SpringApplication.run(WebDiffApplication.class, args);
	}
	
}
