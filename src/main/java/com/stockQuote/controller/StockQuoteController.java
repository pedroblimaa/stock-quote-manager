package com.stockQuote.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.stockQuote.controller.form.StockQuoteForm;
import com.stockQuote.dto.ErrorHandleDto;
import com.stockQuote.dto.StockQuoteDto;
import com.stockQuote.model.Quote;
import com.stockQuote.repository.QuoteRepository;
import com.stockQuote.service.QuoteService;

@RestController
@RequestMapping("/stock-quote")
public class StockQuoteController {

	@Autowired
	private QuoteService quoteService;

	@Autowired
	private QuoteRepository quoteRepo;

	@GetMapping
	public ResponseEntity<List<StockQuoteDto>> list() {

		List<Quote> quotes = quoteRepo.findAll();

		List<StockQuoteDto> stockQuotesDto = quoteService.quotesFormat(quotes);

		return ResponseEntity.ok(stockQuotesDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detail(@PathVariable String id) {

		List<Quote> quotes = quoteRepo.getByStockId(id);
				
		if(quotes.isEmpty()) {
			return new ResponseEntity<ErrorHandleDto>(new ErrorHandleDto("Stock Quote not found", 404),
					HttpStatus.NOT_FOUND);
		}

		StockQuoteDto stockQuoteDto = new StockQuoteDto(id, quotes);

		return ResponseEntity.ok(stockQuoteDto);

	}

	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody StockQuoteForm form, UriComponentsBuilder uriBuilder)
			throws Exception {

		Boolean stockIsValid = quoteService.verifyStockId(form.getStockId());

		if (!stockIsValid) {
			return new ResponseEntity<ErrorHandleDto>(new ErrorHandleDto("Stock not found", 404),
					HttpStatus.BAD_REQUEST);
		}

		Boolean quotesValid = quoteService.verifyQuote(form);
		
		if (!quotesValid) {
			return new ResponseEntity<ErrorHandleDto>(new ErrorHandleDto("A quote with the given date already exists", 409),
					HttpStatus.CONFLICT);
		}

		Map<String, String> quotes = form.getQuotes();
		Quote quoteToSave;
		
		for (Map.Entry<String, String> quote : quotes.entrySet()) {
			quoteToSave = new Quote(LocalDate.parse(quote.getKey()), quote.getValue(), form.getStockId());
			quoteRepo.save(quoteToSave);
		}

		StockQuoteDto stockQuoteDto = new StockQuoteDto(form.getStockId(),
				quoteRepo.getByStockId(form.getStockId()));

		URI uri = URI.create(String.format("/stock-quote/%s", stockQuoteDto.getStockId()));
		return ResponseEntity.created(uri).body(stockQuoteDto);
	}
}
