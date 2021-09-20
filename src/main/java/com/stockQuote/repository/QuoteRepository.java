package com.stockQuote.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockQuote.model.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Long>{

	Optional<Quote> findByQuoteDateAndStockId(LocalDate parse, String stockId);

	List<Quote> getByStockId(String id);

}
