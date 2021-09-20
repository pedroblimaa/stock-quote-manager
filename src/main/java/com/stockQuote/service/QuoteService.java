package com.stockQuote.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.stockQuote.controller.StockController;
import com.stockQuote.controller.form.StockForm;
import com.stockQuote.controller.form.StockQuoteForm;
import com.stockQuote.dto.StockQuoteDto;
import com.stockQuote.model.Quote;
import com.stockQuote.repository.QuoteRepository;

@Configuration
@EnableCaching
@Service
public class QuoteService {

	private QuoteRepository quoteRepo;

	private StockController stockController;

	@Autowired
	public QuoteService(StockController stockController, QuoteRepository quoteRepo) {
		this.stockController = stockController;
		this.quoteRepo = quoteRepo;
	}

	public Boolean verifyStockId(String stockId) {

		List<StockForm> stocks = stockController.getStocks();

		for (StockForm stock : stocks) {
			if (stock.getId().equals(stockId)) {
				return true;
			}
		}

		return false;
	}

	public Boolean verifyQuote(StockQuoteForm form) throws Exception {

		Map<String, String> quotes = form.getQuotes();
		
		Optional<Quote> dbQuote;

		for (Map.Entry<String, String> quote : quotes.entrySet()) {
			dbQuote = quoteRepo.findByQuoteDateAndStockId(LocalDate.parse(quote.getKey()),
					form.getStockId());
			
			if(!dbQuote.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public List<StockQuoteDto> quotesFormat(List<Quote> quotes) {

		List<StockForm> stocks = stockController.getStocks();
		List<StockQuoteDto> stocksDto = new ArrayList<>();

		for (StockForm stock : stocks) {
			stocksDto.add(new StockQuoteDto(stock.getId(), quotes));
		}

		return stocksDto;
	}

}
