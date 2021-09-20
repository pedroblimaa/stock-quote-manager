package com.stockQuote.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.stockQuote.model.Quote;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class QuoteRepositoryTest {

	@Autowired
	private TestEntityManager em;
	
	@Autowired
	private QuoteRepository quoteRepo;

	@Test
	void testShouldGetAllStocks() {

		Quote quote1 = new Quote(LocalDate.parse("2021-09-07"), "50", "petr4");
		Quote quote2 = new Quote(LocalDate.parse("2021-08-07"), "50", "petr4");
		
		em.persist(quote1);
		em.persist(quote2);
		
		List<Quote> quotes = quoteRepo.findAll();

		assertNotNull(quotes);
		assertEquals( LocalDate.parse("2021-09-07") , quotes.get(0).getQuoteDate());
		assertEquals( LocalDate.parse("2021-08-07") , quotes.get(1).getQuoteDate());
	}

}
