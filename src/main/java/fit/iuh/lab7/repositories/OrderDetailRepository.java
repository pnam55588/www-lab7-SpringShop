package fit.iuh.lab7.repositories;

import fit.iuh.lab7.models.Order;
import fit.iuh.lab7.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Order> {
}