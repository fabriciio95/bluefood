package br.com.bluefood.domain.application.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.cliente.ClienteRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.domain.restaurante.SearchFilter;
import br.com.bluefood.domain.restaurante.SearchFilter.Order;
import br.com.bluefood.domain.restaurante.SearchFilter.SearchType;
import br.com.bluefood.util.FileType;
import br.com.bluefood.util.SecurityUtils;

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
	
	public List<Restaurante> search(SearchFilter searchFilter){
		List<Restaurante> restaurantes = new ArrayList<>();
		if(searchFilter.getSearchType().equals(SearchType.Texto)) {
			restaurantes.addAll(restauranteRepository.findByNomeIgnoreCaseContaining(searchFilter.getTexto()));
		} else if(searchFilter.getSearchType().equals(SearchType.Categoria)){
			restaurantes.addAll(restauranteRepository.findByCategorias_Id(searchFilter.getCategoriaId()));
		} else {
			throw new IllegalStateException("O tipo de busca "  + searchFilter.getSearchType() + " não é suportado!");
		}
		
		applyFilters(searchFilter, restaurantes);
		
		return restaurantes;
	}
	
	private void applyFilters(SearchFilter searchFilter, List<Restaurante> restaurantes) {
		Iterator<Restaurante> it = restaurantes.iterator();
		
		while(it.hasNext()) {
			Restaurante restaurante = it.next();
			double taxaEntrega = restaurante.getTaxaEntrega().doubleValue();
			
			if(searchFilter.isEntregaGratis() && taxaEntrega > 0 || !searchFilter.isEntregaGratis() && taxaEntrega == 0) {
				it.remove();
			}
		}
		
		orderFilters(searchFilter, restaurantes, SecurityUtils.getLoggedCliente().getCep());
	}

	private void orderFilters(SearchFilter searchFilter, List<Restaurante> restaurantes, String cep) {
		restaurantes.sort((r1, r2) -> {
			int result;
			if(searchFilter.getOrder() == Order.Taxa) {
				result = r1.getTaxaEntrega().compareTo(r2.getTaxaEntrega());
			} else if(searchFilter.getOrder() == Order.Tempo) {
				result = r1.calcularTempoEntrega(cep).compareTo(r2.calcularTempoEntrega(cep));
			} else {
				throw new IllegalStateException("O valor de ordenação " + searchFilter.getOrder() + " não é válido");
			}
			
			return searchFilter.isAsc() ? result : -result;
		});		
	}
}
