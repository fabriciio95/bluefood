package br.com.bluefood.domain.pagamento;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.bluefood.domain.pedido.Pedido;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@SuppressWarnings("serial")
@Table(name = "pagamento")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pagamento implements Serializable{
	
	@Id
	@EqualsAndHashCode.Include
	private Integer id;
	
	@OneToOne
	@NotNull
	@MapsId
	private Pedido pedido;
	
	@NotNull
	private LocalDateTime data;
	
	@Column(name = "num_cartao_final")
	@NotBlank
	@Size(min = 4, max = 4)
	private String numCartaoFinal;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private BandeiraCartao bandeiraCartao;

	public void definirNumeroEBandeira(String numCartao) {
		numCartaoFinal = numCartao.substring(12);
		bandeiraCartao = getBandeira(numCartao).orElseThrow(() -> new CartaoInvalidoException("Cartão recusado"));
	}
	
	private Optional<BandeiraCartao> getBandeira(String numCartao) {
		if(numCartao.startsWith("0000")) {
			return Optional.of(BandeiraCartao.AMEX);
		} else if(numCartao.startsWith("1111")) {
			return Optional.of(BandeiraCartao.ELO);
		} else if(numCartao.startsWith("2222")) {
			return Optional.of(BandeiraCartao.MASTER);
		} else if(numCartao.startsWith("3333")) {
			return Optional.of(BandeiraCartao.VISA);
		}
		
		return null;
	}
}
