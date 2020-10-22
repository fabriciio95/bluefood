package br.com.bluefood.infrastructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.bluefood.domain.application.service.ClienteService;
import br.com.bluefood.domain.application.service.RestauranteService;
import br.com.bluefood.domain.application.service.ValidationException;
import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.cliente.ClienteRepository;
import br.com.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.domain.restaurante.SearchFilter;
import br.com.bluefood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private ItemCardapioRepository itemCardapioRepository;
	
	@GetMapping(path = "/home")
	public String home(Model model) {
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);		
		model.addAttribute("searchFilter", new SearchFilter());
		return "cliente-home";
	}
	
	@GetMapping(path = "/edit")
	public String edit(Model model) {
		Integer clienteId = SecurityUtils.getLoggedCliente().getId();
		model.addAttribute("cliente", clienteRepository.findById(clienteId).orElseThrow());
		ControllerHelper.setEditeMode(model, true);
		return "cliente-cadastro" ;
	}
	
	@PostMapping(path = "/save")
	public String save(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model) {
		if(!errors.hasErrors()) {
			try {
				clienteService.saveCliente(cliente);
				model.addAttribute("msg", "Cliente atualizado com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		ControllerHelper.setEditeMode(model, true);
		return "cliente-cadastro";
	}
	
	@GetMapping(path = "/search")
	public String search(@ModelAttribute("searchFilter") SearchFilter searchFilter, Model model, @RequestParam(value = "cmd", required = false) String cmd) {
		searchFilter.processFilter(cmd);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		model.addAttribute("restaurantes", restauranteService.search(searchFilter));
		model.addAttribute("searchFilter", searchFilter);
		return "cliente-busca";
	}
	
	@GetMapping("/restaurante")
	public String viewRestaurante(@RequestParam(name = "restauranteId") Integer restauranteId,
			@RequestParam(name = "categoria", required = false) String categoria, Model model) {
		model.addAttribute("restaurante", restauranteRepository.findById(restauranteId).orElseThrow());
		model.addAttribute("categorias", itemCardapioRepository.findCategorias(restauranteId));
		List<ItemCardapio> itensCardapioDestaque;
		List<ItemCardapio> itensCardapioNaoDestaque;
		
		if(categoria == null) {
			itensCardapioDestaque = itemCardapioRepository.findByRestaurante_idAndDestaqueOrderByNome(restauranteId, true);
			itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_idAndDestaqueOrderByNome(restauranteId, false);
		} else {
			itensCardapioDestaque = itemCardapioRepository.findByRestaurante_idAndDestaqueAndCategoriaOrderByNome(restauranteId, true, categoria);			
			itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_idAndDestaqueAndCategoriaOrderByNome(restauranteId, false, categoria);
		}

		model.addAttribute("itensCardapioDestaque", itensCardapioDestaque);
		model.addAttribute("itensCardapioNaoDestaque", itensCardapioNaoDestaque);
		model.addAttribute("categoriaSelecionada", categoria);
		return "cliente-restaurante";
	}
	

}
