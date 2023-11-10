package fit.iuh.lab7;

import fit.iuh.lab7.enums.EmployeeStatus;
import fit.iuh.lab7.enums.ProductStatus;
import fit.iuh.lab7.models.*;
import fit.iuh.lab7.repositories.*;
import net.datafaker.Faker;
import net.datafaker.providers.base.*;
import net.datafaker.providers.base.Number;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@SpringBootApplication
public class Lab7Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab7Application.class, args);
    }
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ProductPriceRepository productPriceRepository;
    @Autowired
    OrderRepository orderRepository;
//    @Bean
    CommandLineRunner createSampleProduct(OrderDetailRepository orderDetailRepository){
        return args -> {
            Faker faker = new Faker();
            Device device =faker.device();
            Number number =faker.number();
            Name name = faker.name();
            Address address = faker.address();
            Random random = new Random();

            for(int i=0;i<200;i++){
                Product product = new Product(
                        device.modelName(),
                        faker.lorem().paragraph(20),
                        "piece",
                        device.manufacturer(),
                        ProductStatus.ACTIVE
                );
                productRepository.save(product);
                ProductPrice productPrice = new ProductPrice(
                        product,
                        LocalDateTime.now(),
                        number.randomDouble(2,100,1000),
                        ""
                );
                productPriceRepository.save(productPrice);
                Customer customer = new Customer(
                    name.name(),
                    "cust_"+i+"@gmail.com",
                    random.nextInt(999999999+1-111111111)+111111111+"",
                        address.fullAddress()
                );
                customerRepository.save(customer);
                Employee employee = new Employee(
                        name.name(),
                        LocalDate.now(),
                        "emp_"+i+"@gmail.com",
                        random.nextInt(999999999+1-111111111)+111111111+"",
                        address.fullAddress(),
                        EmployeeStatus.ACTIVE
                );
                employeeRepository.save(employee);
                Order order = new Order(
                        LocalDateTime.now(),
                        employee,
                        customer
                );
                orderRepository.save(order);
                OrderDetail orderDetail = new OrderDetail(
                        random.nextInt(3),
                        number.randomDouble(2,100,1000),
                        "",
                        order,
                        product);
                orderDetailRepository.save(orderDetail);

            }
        };
    }
}
