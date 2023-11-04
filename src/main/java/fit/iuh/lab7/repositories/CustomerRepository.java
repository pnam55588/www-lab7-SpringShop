package fit.iuh.lab7.repositories;

import fit.iuh.lab7.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}