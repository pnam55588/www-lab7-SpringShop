package fit.iuh.lab7.repositories;

import fit.iuh.lab7.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}