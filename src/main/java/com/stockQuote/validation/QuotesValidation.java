package com.stockQuote.validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.stockQuote.controller.StockController;
import com.stockQuote.validation.constraint.QuoteIsValid;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuotesValidation implements ConstraintValidator<QuoteIsValid, Map<String, String>> {

	@Override
	public void initialize(QuoteIsValid quotes) {
	}

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StockController.class);

	@Override
	public boolean isValid(Map<String, String> quotes, ConstraintValidatorContext context) {

		context.disableDefaultConstraintViolation();

		if (quotes == null) {
			context.buildConstraintViolationWithTemplate("Must not be null").addConstraintViolation();
			return false;
		}

		boolean isValid = true;

		List<String> list1 = new ArrayList<>();
		List<String> list2 = new ArrayList<>();

		for (Map.Entry<String, String> quote : quotes.entrySet()) {
			try {
				LocalDate.parse(quote.getKey());
			} catch (Exception e) {
				context.buildConstraintViolationWithTemplate("Invalid date").addConstraintViolation();
				isValid = false;
			}

			try {
				Integer.parseInt(quote.getValue());
			} catch (Exception e) {
				context.buildConstraintViolationWithTemplate("Invalid value").addConstraintViolation();
				isValid = false;
			}

			list1.add(quote.getKey());
			list2.add(quote.getKey());
		}

		return isValid;
	}

}
