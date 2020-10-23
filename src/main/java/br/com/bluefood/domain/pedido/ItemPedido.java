package br.com.bluefood.domain.pedido;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.bluefood.domain.restaurante.ItemCardapio;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@SuppressWarnings("serial")
@Table(name = "item_pedido")
public class ItemPedido implements Serializable {
	
	@EmbeddedId
	@EqualsAndHashCode.Include
	private ItemPedidoPK id;
	
	@NotNull
	@ManyToOne
	private ItemCardapio itemCardapio;
	
	@NotNull
	private Integer quantidade;
	
	@Size(max = 50)
	private String observacoes;
	
	@NotNull
	private BigDecimal preco;
	
	public BigDecimal getPrecoCalculado() {
		return preco.multiply(BigDecimal.valueOf(quantidade));
	}
}
