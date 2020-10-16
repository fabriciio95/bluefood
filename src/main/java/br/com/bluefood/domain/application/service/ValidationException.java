package br.com.bluefood.domain.application.service;

@SuppressWarnings("serial")
public class ValidationException extends Exception{

	
	public ValidationException(String msg) {
		super(msg);
	}
	
}
