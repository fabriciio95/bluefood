package br.com.bluefood.infrastructure.web.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import br.com.bluefood.domain.pedido.Carrinho;
import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.domain.pedido.PedidoRepository;
import br.com.bluefood.domain.pedido.RestauranteDiferenteException;
import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;

@Controller
@RequestMapping(path = "/cliente/carrinho")
@SessionAttributes("carrinho")
public class CarrinhoController {
	
	
	@Autowired
	private ItemCardapioRepository itemCardapioRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@GetMapping("/visualizar")
	public String visualizar() {
		return "cliente-carrinho";
	}
	
	
	@GetMapping("/adicionar")
	public String adicionarItem(@RequestParam("itemId") Integer itemId, @RequestParam("quantidade") Integer quantidade,
			@RequestParam("observacoes") String observacoes, @ModelAttribute("carrinho") Carrinho carrinho, Model model) {
		ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow(NoSuchElementException::new);
		try {
			carrinho.addItem(itemCardapio, quantidade, observacoes);
		} catch(RestauranteDiferenteException e) {
			model.addAttribute("msg", "Não é possivel misturar comidas de restaurantes diferentes");
		}
		return "cliente-carrinho";
	}
	
	@GetMapping("/remover")
	public String removerItem(@RequestParam("itemId") Integer itemId, @ModelAttribute("carrinho") Carrinho carrinho, 
			SessionStatus sessionStatus, Model model) {
		carrinho.removeItem(itemCardapioRepository.findById(itemId).orElseThrow(NoSuchElementException::new));
		if(carrinho.vazio()) {
			sessionStatus.setComplete();
		}
		return "cliente-carrinho";
	}
	
	@GetMapping("/refazerCarrinho")
	public String refazerCarrinho(@RequestParam("pedidoId") Integer pedidoId, @ModelAttribute("carrinho") Carrinho carrinho,
			Model model) {
		Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(NoSuchElementException::new);
		carrinho.limpar();
		pedido.getItens().forEach((item) -> carrinho.addItem(item));
		return "cliente-carrinho";
	}
	
	@ModelAttribute("carrinho")
	public Carrinho carrinho() {
		return new Carrinho();
	}
}
