package br.com.bluefood.domain.pedido;

import java.math.BigDecimal;

import javax.persistence.Entity;

import br.com.bluefood.domain.restaurante.ItemCardapio;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedido {
	
	@EqualsAndHashCode.Include
	private Integer id;

	private ItemCardapio itemCardapio;
	
	private Integer quantidade;
	
	private String observacoes;
	
	private BigDecimal preco;
}
