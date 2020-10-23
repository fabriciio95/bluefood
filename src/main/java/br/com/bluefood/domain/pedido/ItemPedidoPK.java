package br.com.bluefood.domain.pedido;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ItemPedidoPK implements Serializable {

	@NotNull
	@ManyToOne
	private Pedido pedido;
	
	@NotNull
	private Integer ordem;
}
