package fit.iuh.lab7.repositories;

import fit.iuh.lab7.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}