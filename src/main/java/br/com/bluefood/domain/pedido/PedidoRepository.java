package br.com.bluefood.domain.pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

	List<Pedido> findByCliente_id(Integer idCliente);
	
	@Query("SELECT p FROM Pedido p WHERE p.cliente.id = ?1 ORDER BY p.data DESC")
	List<Pedido> getPedidosPorCliente(Integer idCliente);
}
