package br.com.bluefood.domain.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.cliente.ClienteRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteRepository;

@Service
public class RestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Transactional
	public void saveRestaurante(Restaurante restaurante) throws ValidationException {
		
		if(!validateEmail(restaurante.getEmail(), restaurante.getId())) {
			throw new ValidationException("O e-mail está duplicado");
		}
		
		if(restaurante.getId() != null) {
			Restaurante restauranteDB = restauranteRepository.findById(restaurante.getId()).orElseThrow();
			restaurante.setSenha(restauranteDB.getSenha());
			restauranteRepository.save(restaurante);
		} else {
			restaurante.encryptPassword();
			restaurante = restauranteRepository.save(restaurante);
			restaurante.setLogotipoFileName();
			restauranteRepository.save(restaurante);
			imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
		}		
	}
	
	private boolean validateEmail(String email, Integer id) {
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if(cliente != null) {
			return false;
		}
		
		Restaurante restaurante = restauranteRepository.findByEmail(email);
		
		if(restaurante != null) {
			
			if(id == null) {
				return false;
			}
			
			if(!restaurante.getId().equals(id)) {
				return false;
			}
		}
		
		
		return true;
	}
}
