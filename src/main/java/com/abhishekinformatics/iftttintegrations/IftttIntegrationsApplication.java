package com.abhishekinformatics.iftttintegrations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class IftttIntegrationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IftttIntegrationsApplication.class, args);
	}

	@Bean("restTemplate")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean("objectMapper")
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}
