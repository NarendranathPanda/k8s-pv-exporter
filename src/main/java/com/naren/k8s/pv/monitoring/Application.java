package com.naren.k8s.pv.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
		LOGGER.info("k8s pv exporter Application Started.");
	}

	@Override
	public void run(String... args) throws Exception {

	}

}