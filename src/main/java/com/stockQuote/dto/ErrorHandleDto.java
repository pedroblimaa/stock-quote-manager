package com.stockQuote.dto;

public class ErrorHandleDto {

	private String message;
	private int code;

	public ErrorHandleDto(String message, int code) {
		super();
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

}
