package br.com.bluefood.domain.application.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bluefood.domain.pedido.Carrinho;
import br.com.bluefood.domain.pedido.ItemPedido;
import br.com.bluefood.domain.pedido.ItemPedidoPK;
import br.com.bluefood.domain.pedido.ItemPedidoRepository;
import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.domain.pedido.Pedido.Status;
import br.com.bluefood.domain.pedido.PedidoRepository;
import br.com.bluefood.util.SecurityUtils;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public Pedido criarEPagar(Carrinho carrinho, String numCartao) {
		Pedido pedido = new Pedido();
		pedido.setData(LocalDateTime.now());
		pedido.setCliente(SecurityUtils.getLoggedCliente());
		pedido.setRestaurante(carrinho.getRestaurante());
		pedido.setStatus(Status.Producao);
		pedido.setTaxaEntrega(carrinho.getRestaurante().getTaxaEntrega());
		pedido.setSubtotal(carrinho.getPrecoTotal(false));
		pedido.setTotal(carrinho.getPrecoTotal(true));
		pedido.getItens().addAll(carrinho.getItens());
		pedidoRepository.save(pedido);

		int ordem = 1;
		
		for (ItemPedido itemPedido : carrinho.getItens()) {
			itemPedido.setId(new ItemPedidoPK(pedido, ordem++));
			itemPedidoRepository.save(itemPedido);
		}
		
		//TODO: Pagamento
		
		return pedido;
		
	}
}
