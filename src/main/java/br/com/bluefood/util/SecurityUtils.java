package br.com.bluefood.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.infrastructure.web.security.LoggedUser;

public class SecurityUtils {

	public static LoggedUser getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}
		
		return (LoggedUser) authentication.getPrincipal();
	}
	
	public static Cliente getLoggedCliente() {
		LoggedUser loggedUser = getLoggedUser();
		if(loggedUser == null) {
			throw new IllegalStateException("Não existe um usuário logado");
		}
		
		if(!(loggedUser.getUsuario() instanceof Cliente)) {
			throw new IllegalStateException("O usuário logado não é um cliente");
		}
		
		return (Cliente) loggedUser.getUsuario();
	}
	
	public static Restaurante getLoggedRestaurante() {
		LoggedUser loggedUser = getLoggedUser();
		
		if(loggedUser == null) {
			throw new IllegalStateException("Não existe um usuário logado");
		}
		
		if(!(loggedUser.getUsuario() instanceof Restaurante)) {
			throw new IllegalStateException("O usuário logado não é um restaurante");
		}
		
		return (Restaurante) loggedUser.getUsuario();
	}
}
