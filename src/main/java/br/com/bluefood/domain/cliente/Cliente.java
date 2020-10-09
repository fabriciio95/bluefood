package br.com.bluefood.domain.cliente;

import javax.persistence.Entity;

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

	private String cpf;
	
	private String cep;
}
