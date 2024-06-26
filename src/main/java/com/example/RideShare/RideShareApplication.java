package com.example.RideShare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RideShareApplication {
	public static Logger LOG = LoggerFactory.getLogger(RideShareApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RideShareApplication.class, args);
		LOG.info("APPLICATION IS RUNNING");
	}
}