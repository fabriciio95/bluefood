package br.com.bluefood.domain.pagamento;

@SuppressWarnings("serial")
public class CartaoInvalidoException extends RuntimeException {

	public CartaoInvalidoException() {
	}

	public CartaoInvalidoException(String message) {
		super(message);
	}

	public CartaoInvalidoException(Throwable cause) {
		super(cause);
	}

	public CartaoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}
}
