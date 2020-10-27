package br.com.bluefood.domain.application.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.bluefood.domain.pagamento.Cartao;
import br.com.bluefood.domain.pagamento.StatusPagamento;
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

	@Value("${bluefood.bfpay.url}")
	private String bfPayUrl;

	@Value("${bluefood.bfpay.token}")
	private String bfPayToken;

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = PagamentoException.class)
	public Pedido criarEPagar(Carrinho carrinho, String numCartao) throws PagamentoException {
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

		Cartao cartao = new Cartao();
		cartao.setNumCartao(numCartao);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Token", bfPayToken);

		HttpEntity<Cartao> requestEntity = new HttpEntity<>(cartao, headers);

		RestTemplate restTemplate = new RestTemplate();
		
		Map<String, String> response;
		try {
			response = restTemplate.postForObject(bfPayUrl, requestEntity, Map.class);
		} catch (Exception e) {
			throw new PagamentoException("Erro no servidor de pagamento");
		}
		StatusPagamento statusPagamento = StatusPagamento.valueOf(response.get("status"));
		System.out.println(statusPagamento.name());
		if (statusPagamento != StatusPagamento.Autorizado) {
			throw new PagamentoException(statusPagamento.getDescricao());
		}

		return pedido;

	}
}
