package com.stockQuote.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.stockQuote.controller.form.StockForm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/stockcache")
public class StockController {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StockController.class);

	@Value("${stock-manager.url}") String defaultUrl;
	
	@Cacheable("getStocks")
	public List<StockForm> getStocks() {

		RestTemplate restTemplate = new RestTemplate();
		StockForm[] stockForm = restTemplate.getForObject(defaultUrl + "/stock", StockForm[].class);

		log.info("Fetching API data...");
		
		return Arrays.asList(stockForm);
	}

	@DeleteMapping
	@CacheEvict("getStocks")
	public void clearCache() {
		log.info("Clearing cache...");
	}
}
