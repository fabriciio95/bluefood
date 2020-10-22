package br.com.bluefood.domain.restaurante;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Integer>{

	@Query("SELECT DISTINCT ic.categoria FROM ItemCardapio ic WHERE ic.restaurante.id = ?1 ORDER BY ic.categoria")
	public List<String> findCategorias(Integer restauranteId);
	
	public List<ItemCardapio> findByRestaurante_idOrderByNome(Integer id);
	
	public List<ItemCardapio> findByRestaurante_idAndDestaqueOrderByNome(Integer id, boolean destaque);
	
	public List<ItemCardapio> findByRestaurante_idAndDestaqueAndCategoriaOrderByNome(Integer id, boolean destaque, String categoria);
}
