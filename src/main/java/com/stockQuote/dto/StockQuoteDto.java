package com.stockQuote.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.stockQuote.model.Quote;

public class StockQuoteDto {

	private UUID id = UUID.randomUUID();
	private String stockId;
	private Map<String, String> quotes = new HashMap<String, String>();
	
	public StockQuoteDto(String stockId, List<Quote> quotes) {
		this.stockId = stockId;
		
		for(Quote quote: quotes) {
			if(quote.getStockId().equals(stockId)) {
				this.quotes.put(quote.getQuoteDate().toString(), quote.getValue());
			};
		}
		
	}

	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getStockId() {
		return stockId;
	}
	
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	
	public Map<String, String> getQuotes() {
		return quotes;
	}
	public void setQuotes(Map<String, String> quotes) {
		this.quotes = quotes;
	}
	
	
}
