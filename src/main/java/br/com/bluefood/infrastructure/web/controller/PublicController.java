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

import br.com.bluefood.domain.application.ClienteService;
import br.com.bluefood.domain.application.ValidationException;
import br.com.bluefood.domain.cliente.Cliente;

@Controller
@RequestMapping(path = "/public")
public class PublicController {
	
	@Autowired
	private ClienteService clienteService;

	@GetMapping("/cliente/new")
	public String newCliente(Model model) {
		model.addAttribute("cliente", new Cliente());
		ControllerHelper.setEditeMode(model, false);
		return "cliente-cadastro";
	}
	
	@PostMapping("/cliente/save")
	public String saveCliente(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model) {
		if(!errors.hasErrors()) {
			try {
			clienteService.saveCliente(cliente);
			model.addAttribute("msg", "Cliente cadastrado com sucesso!");
			} catch(ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		} 
		
		ControllerHelper.setEditeMode(model, false);
		return "cliente-cadastro";
	}
}
