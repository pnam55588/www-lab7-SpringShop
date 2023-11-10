package fit.iuh.lab7.services;

import fit.iuh.lab7.models.Customer;
import fit.iuh.lab7.repositories.CustomerRepository;
import fit.iuh.lab7.repositories.ProductRepository;
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
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    public Optional<Customer> findById(long id){return  customerRepository.findById(id);};
    public Page<Customer> findAll(int page, int size, String orderBy, String softDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(softDirection),orderBy);
        Pageable pageable = PageRequest.of(page,size,sort);
        return customerRepository.findAll(pageable);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void delete(long id) {
        customerRepository.deleteById(id);
    }


}
