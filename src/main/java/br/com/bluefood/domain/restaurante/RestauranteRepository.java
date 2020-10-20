package br.com.bluefood.domain.restaurante;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Integer> {
	
	Restaurante findByEmail(String email);
	 
	List<Restaurante> findByNomeIgnoreCaseContaining(String nome);
	
	List<Restaurante> findByCategorias_Id(Integer categoriaId);
	
}
