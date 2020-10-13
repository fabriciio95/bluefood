package br.com.bluefood.domain.cliente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import br.com.bluefood.domain.usuario.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@SuppressWarnings("serial")
public class Cliente extends Usuario {
	
	@NotBlank(message = " O CPF n�o pode ser vazio")
	@Pattern(regexp = "[0-9]{11}", message = "O CPF possui formato inv�lido!")
	@Column(length = 11, unique = true, nullable = false)
	private String cpf;
	
	@NotBlank(message = " O CEP n�o pode ser vazio")
	@Pattern(regexp = "[0-9]{8}", message = "O CEP possui formato inv�lido!")
	@Column(length = 8, nullable = false)
	private String cep;
}
