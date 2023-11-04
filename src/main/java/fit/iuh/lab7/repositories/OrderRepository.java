package fit.iuh.lab7.repositories;

import fit.iuh.lab7.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}