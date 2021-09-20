package com.stockQuote;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
public class StockQuoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockQuoteApplication.class, args);
	}
	
	@Value("${stock-manager.url}") String defaultUrl;

	@Bean
	public CommandLineRunner CommandLineRunnerBean() throws URISyntaxException {

		String registerUrl = defaultUrl + "/notification";
		URI uri = new URI(registerUrl);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String json = "{\"host\":\"localhost\", \"port\": \"8081\"}";
		

		HttpEntity<String> entity = new HttpEntity<String>(json, headers);
		restTemplate.postForEntity(uri, entity, null);
		return null;
	}
}
