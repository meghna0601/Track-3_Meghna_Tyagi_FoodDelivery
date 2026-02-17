package com.restraunt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RestrauntApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestrauntApplication.class, args);
	}

}
