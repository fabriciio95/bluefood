package br.com.bluefood.domain.application;

@SuppressWarnings("serial")
public class ValidationException extends Exception{

	
	public ValidationException(String msg) {
		super(msg);
	}
	
}
