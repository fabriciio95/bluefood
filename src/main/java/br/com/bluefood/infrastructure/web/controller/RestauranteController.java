package br.com.bluefood.infrastructure.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluefood.domain.application.service.RestauranteService;
import br.com.bluefood.domain.application.service.ValidationException;
import br.com.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/restaurante")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@Autowired
	private RestauranteService restauranteService;

	@GetMapping(path = "/home")
	public String home() {
		return "restaurante-home";
	}
	
	@GetMapping(path = "/edit")
	public String edit(Model model) {
		Integer restauranteId = SecurityUtils.getLoggedRestaurante().getId();
		model.addAttribute("restaurante", restauranteRepository.findById(restauranteId).orElseThrow());
		ControllerHelper.setEditeMode(model, true);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		return "restaurante-cadastro" ;
	}
	
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("restaurante") Restaurante restaurante,
			Errors errors,
			Model model) {
		if(!errors.hasErrors()) {
			try {
				restauranteService.saveRestaurante(restaurante);
				model.addAttribute("msg", "Restaurante gravado com sucesso!");
			} catch(ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		
		ControllerHelper.setEditeMode(model, true);
		SecurityUtils.getLoggedUser().atualizarLoggedUser(restaurante);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		return "restaurante-cadastro";
	}
}
