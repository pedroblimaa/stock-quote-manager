package com.stockQuote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.stockQuote.controller.StockController;
import com.stockQuote.controller.form.StockForm;
import com.stockQuote.controller.form.StockQuoteForm;
import com.stockQuote.dto.StockQuoteDto;
import com.stockQuote.model.Quote;
import com.stockQuote.repository.QuoteRepository;

class QuoteServiceTest {

	private QuoteService quoteService;

	private QuoteRepository quoteRepo;

	private StockController stockController;

	@BeforeEach
	private void beforeEach() {
		quoteRepo = Mockito.mock(QuoteRepository.class);
		stockController = Mockito.mock(StockController.class);

		quoteService = new QuoteService(stockController, quoteRepo);
	}

	// --------------- VerifyQuote ------------------------

	@Test
	void testShouldReturnTrueWhenVerifingQuote() throws Exception {

		Map<String, String> quotes = new HashMap<>();

		quotes.put("2020-12-20", "30");
		quotes.put("2020-12-21", "40");
		quotes.put("2020-12-22", "50");

		StockQuoteForm form = new StockQuoteForm("petr4", quotes);

		Mockito.when(quoteRepo.findByQuoteDateAndStockId(Mockito.any(LocalDate.class), Mockito.anyString()))
				.thenReturn(Optional.empty());

		Boolean quoteIsValid = quoteService.verifyQuote(form);

		assertTrue(quoteIsValid);
	}

	@Test
	void testSouldReturnFalseWhenVerifingQuote() throws Exception {

		Quote newQuote = new Quote(LocalDate.parse("2021-05-20"), "petr4", "50");

		Map<String, String> quotes = new HashMap<>();

		quotes.put("2021-05-20", "30");
		quotes.put("2020-12-21", "40");
		quotes.put("2020-12-22", "50");

		StockQuoteForm form = new StockQuoteForm("petr4", quotes);

		Mockito.when(quoteRepo.findByQuoteDateAndStockId(Mockito.any(LocalDate.class), Mockito.anyString()))
				.thenReturn(Optional.of(newQuote));

		Boolean quoteIsValid = quoteService.verifyQuote(form);

		assertTrue(!quoteIsValid);
	}

	// --------------- VerifyStockId ------------------------

	@Test
	void testSouldReturnTrueToStockId() {

		List<StockForm> stocks = createStocks();
		Mockito.when(stockController.getStocks()).thenReturn(stocks);

		Boolean validId = quoteService.verifyStockId("petr4");
		assertEquals(true, validId);
	}

	@Test
	void testSouldReturnFalseToStockId() {

		List<StockForm> stocks = createStocks();
		Mockito.when(stockController.getStocks()).thenReturn(stocks);

		Boolean validId = quoteService.verifyStockId("petr5");
		assertEquals(false, validId);
	}

	// --------------- QuotesFormat ------------------------

	@Test
	void testSouldReturnAFormattedList() {

		List<StockForm> stocks = createStocks();

		Mockito.when(stockController.getStocks()).thenReturn(stocks);
		Mockito.when(stockController.getStocks()).thenReturn(stocks);

		List<Quote> quotes = createQuotes();
		List<StockQuoteDto> quotesFormat = quoteService.quotesFormat(quotes);

		assertTrue(!quotesFormat.isEmpty());
		assertEquals(2, quotesFormat.size());
		assertEquals(stocks.get(0).getId(), quotesFormat.get(0).getStockId());
		assertEquals(stocks.get(1).getId(), quotesFormat.get(1).getStockId());
		assertEquals(2, quotesFormat.get(0).getQuotes().size());
		assertEquals(1, quotesFormat.get(1).getQuotes().size());
	}

	@Test
	void testSouldReturnAEmptyList() {

		Mockito.when(stockController.getStocks()).thenReturn(new ArrayList<>());

		List<Quote> quotes = new ArrayList<>();
		List<StockQuoteDto> quotesFormat = quoteService.quotesFormat(quotes);

		assertTrue(quotesFormat.isEmpty());
	}

	private List<Quote> createQuotes() {

		List<Quote> quotes = new ArrayList<>();
		Quote quote1 = new Quote(LocalDate.parse("2021-04-05"), "35", "petr4");
		Quote quote2 = new Quote(LocalDate.parse("2021-04-08"), "40", "petr4");
		Quote quote3 = new Quote(LocalDate.parse("2021-04-08"), "30", "vale5");
		quotes.add(quote1);
		quotes.add(quote2);
		quotes.add(quote3);

		return quotes;
	}

	private List<StockForm> createStocks() {

		List<StockForm> stocks = new ArrayList<>();
		StockForm stock1 = new StockForm();
		stock1.setId("petr4");
		StockForm stock2 = new StockForm();
		stock2.setId("vale5");
		stocks.add(stock1);
		stocks.add(stock2);

		return stocks;
	}

}
