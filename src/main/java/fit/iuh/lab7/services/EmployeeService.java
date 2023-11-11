package fit.iuh.lab7.services;

import fit.iuh.lab7.models.Customer;
import fit.iuh.lab7.models.Employee;
import fit.iuh.lab7.repositories.CustomerRepository;
import fit.iuh.lab7.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    public Optional<Employee> findById(long id){return  employeeRepository.findById(id);};
    public Page<Employee> findAll(int page, int size, String orderBy, String softDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(softDirection),orderBy);
        Pageable pageable = PageRequest.of(page,size,sort);
        return employeeRepository.findAll(pageable);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(long id) {
        employeeRepository.deleteById(id);
    }
}
