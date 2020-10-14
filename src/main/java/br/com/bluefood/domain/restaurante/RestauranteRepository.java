package br.com.bluefood.domain.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Integer> {
	
	Restaurante findByEmail(String email);

}
