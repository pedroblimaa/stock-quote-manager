package com.stockQuote.controller.form;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.stockQuote.validation.constraint.QuoteIsValid;

public class StockQuoteForm {

	@NotEmpty
	@NotNull
	private String stockId;
	@NotEmpty
	@QuoteIsValid
	private Map<String, String> quotes;

	public StockQuoteForm(String stockId, Map<String, String> quotes) {
		this.stockId = stockId;
		this.quotes = quotes;
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
