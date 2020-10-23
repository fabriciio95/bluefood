package br.com.bluefood.infrastructure.web.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.usuario.Usuario;

@SuppressWarnings("serial")
public class LoggedUser implements UserDetails {
	
	private Usuario usuario;
	private Role role;
	private Collection<? extends GrantedAuthority> roles;
	
	public LoggedUser(Usuario usuario) {
		this.usuario = usuario;
		if(usuario instanceof Cliente) {
			this.role = Role.CLIENTE;
		} else if(usuario instanceof Restaurante) {
			this.role = Role.RESTAURANTE;
		} else {
			throw new IllegalStateException("O tipo de usu�rio n�o � v�lido!");
		}
		
		this.roles = List.of(new SimpleGrantedAuthority("ROLE_" + role));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Role getRole() {
		return role;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void atualizarLoggedUser(Usuario usuario) {
		this.usuario.setNome(usuario.getNome());
		this.usuario.setEmail(usuario.getEmail());
		this.usuario.setTelefone(usuario.getTelefone());
	}
}
