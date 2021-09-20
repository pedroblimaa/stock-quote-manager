package com.stockQuote.validation.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.stockQuote.validation.QuotesValidation;

@Documented
@Constraint(validatedBy = QuotesValidation.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface QuoteIsValid {
	String message() default "Invalid quotes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}