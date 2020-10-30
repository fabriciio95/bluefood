package br.com.bluefood.infrastructure.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.bluefood.domain.cliente.ClienteRepository;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.domain.usuario.Usuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = clienteRepository.findByEmail(username);
		
		if(usuario == null) {
			usuario = restauranteRepository.findByEmail(username);
			
			if(usuario == null) {
				throw new UsernameNotFoundException("Usuário inválido");
			}
		}
		
		return new LoggedUser(usuario);
	}

}
