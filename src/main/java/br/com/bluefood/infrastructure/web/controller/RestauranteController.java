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

import br.com.bluefood.domain.application.service.RelatorioService;
import br.com.bluefood.domain.application.service.RestauranteService;
import br.com.bluefood.domain.application.service.ValidationException;
import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.domain.pedido.Pedido.Status;
import br.com.bluefood.domain.pedido.PedidoRepository;
import br.com.bluefood.domain.pedido.RelatorioPedidoFilter;
import br.com.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/restaurante")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private RelatorioService relatorioService;
	
	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private ItemCardapioRepository itemCardapioRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	

	@GetMapping(path = "/home")
	public String home(Model model) {
		Integer restauranteId = SecurityUtils.getLoggedRestaurante().getId();
		List<Pedido> pedidos = pedidoRepository.findByRestaurante_idAndStatusNotOrderByDataDesc(restauranteId, Status.Concluido);
		model.addAttribute("pedidos", pedidos);
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
	
	@GetMapping("/comidas")
	public String viewComidas(Model model) {
		addDependenciesForViewRestauranteComidas(model, true);
		return "restaurante-comidas";
	}
	
	@GetMapping("/comidas/remover")
	public String remover(Model model, @RequestParam("itemId") Integer itemId) {
		restauranteService.removerItemCardapio(itemId);
		model.addAttribute("msg", "Item removido com sucesso!");
		addDependenciesForViewRestauranteComidas(model, true);
		return "restaurante-comidas";
	}
	
	@PostMapping("/comidas/cadastrar")
	public String cadastrarItemCardapio(@ModelAttribute("itemCardapio") @Valid ItemCardapio itemCardapio,
			Errors errors,
			Model model) {
		
		if(errors.hasErrors()) {
			addDependenciesForViewRestauranteComidas(model, false);
			return "restaurante-comidas";
		}
		restauranteService.cadastrarItemCardapio(itemCardapio);
		model.addAttribute("msg", "O item " + itemCardapio.getNome() + " foi cadastrado com sucesso!");
		addDependenciesForViewRestauranteComidas(model, true);
		return "restaurante-comidas";
	}
	
	@GetMapping("/comidas/destaque")
	public String destaque(@RequestParam(name = "itemId") Integer itemId, @RequestParam(name = "destaque") boolean destaque, Model model) {
		ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow();
		itemCardapio.setDestaque(destaque);
		itemCardapioRepository.save(itemCardapio);
		if(destaque) {
			model.addAttribute("msg", "Item colocado em destaque");
		} else {
			model.addAttribute("msg", "O item foi retirado de destaque");
		}
		addDependenciesForViewRestauranteComidas(model, true);
		return "restaurante-comidas";
	}
	
	@GetMapping("/pedido")
	public String viewPedido(@RequestParam("pedidoId") Integer pedidoId ,Model model) {
		model.addAttribute("pedido", pedidoRepository.findById(pedidoId).orElseThrow());
		return "restaurante-pedido";
	}
	
	@PostMapping("/pedido/proximoStatus")
	public String proximoStatus(@RequestParam("pedidoId") Integer pedidoId, Model model) {
		Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();
		pedido.definirProximoStatus();
		pedidoRepository.save(pedido);
		model.addAttribute("pedido", pedido);
		model.addAttribute("msg", "Status alterado com sucesso");
		return "restaurante-pedido";
	}
	
	@GetMapping("/relatorio/pedidos")
	public String viewRelatorioPedidos(@ModelAttribute("relatorioPedidoFilter") RelatorioPedidoFilter filter,
			Model model) {
		Integer restauranteId = SecurityUtils.getLoggedRestaurante().getId();
		List<Pedido> pedidos = relatorioService.listPedidos(restauranteId, filter);
		model.addAttribute("filter", filter);
		model.addAttribute("pedidos", pedidos);
		return "restaurante-relatorio-pedidos";
	}
	
	private void addDependenciesForViewRestauranteComidas(Model model,  boolean withNewItemCardapio) {
		Integer restauranteId = SecurityUtils.getLoggedRestaurante().getId();
		if(withNewItemCardapio) {
			model.addAttribute("itemCardapio", new ItemCardapio());
		}
		model.addAttribute("itensCardapio", itemCardapioRepository.findByRestaurante_idOrderByNome(restauranteId));
		model.addAttribute("restaurante", restauranteRepository.findById(restauranteId).orElseThrow());
	}
}
