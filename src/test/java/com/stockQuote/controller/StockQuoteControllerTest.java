package com.stockQuote.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.stockQuote.controller.form.StockForm;
import com.stockQuote.model.Quote;
import com.stockQuote.repository.QuoteRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StockQuoteControllerTest {

	@MockBean
	private StockController stockController;

	@MockBean
	private QuoteRepository quoteRepo;

	@Autowired
	private MockMvc mockMvc;

	private List<StockForm> stocks;

	private URI uri;

	@BeforeEach
	void beforeEach() throws URISyntaxException {
		stocks = new ArrayList<>();

		StockForm stock1 = new StockForm();
		stock1.setId("vale5");

		StockForm stock2 = new StockForm();
		stock2.setId("petr4");

		stocks.add(stock1);
		stocks.add(stock2);

		Mockito.when(stockController.getStocks()).thenReturn(stocks);

		uri = new URI("/stock-quote");
	}

	@Test
	void testSouldListQuotesEmpty() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].quotes.size()", Matchers.is(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].quotes.size()", Matchers.is(0)));
	}

	@Test
	void testSouldListQuotesSuccess() throws Exception {

		List<Quote> quotes = new ArrayList<>();

		Quote quote1 = new Quote(LocalDate.parse("2021-09-07"), "50", "petr4");
		Quote quote2 = new Quote(LocalDate.parse("2021-08-07"), "30", "petr4");
		Quote quote3 = new Quote(LocalDate.parse("2021-08-07"), "60", "vale5");

		quotes.add(quote1);
		quotes.add(quote2);
		quotes.add(quote3);

		Mockito.when(quoteRepo.findAll()).thenReturn(quotes);

		this.mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].quotes.size()", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].quotes.size()", Matchers.is(2)));
	}

	@Test
	void testShouldDetailAStock() throws Exception {

		List<Quote> quotes = new ArrayList<>();

		Quote quote1 = new Quote(LocalDate.parse("2021-09-07"), "50", "petr4");
		Quote quote2 = new Quote(LocalDate.parse("2021-08-07"), "30", "petr4");

		quotes.add(quote1);
		quotes.add(quote2);

		Mockito.when(quoteRepo.getByStockId(Mockito.anyString())).thenReturn(quotes);

		this.mockMvc.perform(MockMvcRequestBuilders.get(uri + "/petr4"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.stockId").value("petr4"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quotes.size()", Matchers.is(2)));
	}

	@Test
	void testShouldNotDetailAStockBecouseIdIsInvalid() throws Exception {

		List<Quote> quotes = new ArrayList<>();

		Mockito.when(quoteRepo.getByStockId(Mockito.anyString())).thenReturn(new ArrayList<>());

		this.mockMvc.perform(MockMvcRequestBuilders.get(uri + "/petr5"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	void testShouldRegisterAStockQuoteAndReturnIt() throws Exception {
		
		String stockId = "petr4";
		
		Quote quote1 = new Quote(LocalDate.parse("2021-09-07"), "25", "petr4");
		Quote quote2 = new Quote(LocalDate.parse("2021-09-08"), "35", "petr4");
		
		JSONObject quotes = new JSONObject();
		quotes.put(quote1.getQuoteDate().toString(), quote1.getValue());
		quotes.put(quote2.getQuoteDate().toString(), quote2.getValue());
		
		JSONObject json = new JSONObject();
		json.put("stockId", stockId);
		json.put("quotes", quotes);
		
		List<Quote> quotesList = new ArrayList<>();
		quotesList.add(quote1);
		quotesList.add(quote2);
		
		Mockito.when(quoteRepo.getByStockId(Mockito.anyString())).thenReturn(quotesList);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json.toString())	
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.stockId").value(stockId))
		.andExpect(MockMvcResultMatchers.jsonPath("$.quotes.size()", Matchers.is(2)));
	}
	
	@Test
	void testShouldNotRegisterAStockQuoteBecouseKeysAreInvalid() throws Exception {
		
		String stockId = "petr4";
		
		JSONObject quotes = new JSONObject();
		quotes.put("2021-09-07", "25");
		quotes.put("2021-09-08", "35");
		
		JSONObject json = new JSONObject();
		json.put("stockIdd", stockId);
		json.put("quotes", quotes);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json.toString())	
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	void testShouldNotRegisterAStockBecouseDateIsInvalid() throws Exception {
		
		String stockId = "petr4";
		
		JSONObject quotes = new JSONObject();
		quotes.put("2021-09-07", "25");
		quotes.put("2021-09-0889", "35");
		
		JSONObject json = new JSONObject();
		json.put("stockIdd", stockId);
		json.put("quotes", quotes);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json.toString())	
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
