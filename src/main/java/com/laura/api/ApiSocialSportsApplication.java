package com.laura.api;

import com.laura.api.storage.StorageProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ApiSocialSportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSocialSportsApplication.class, args);
	}

}
