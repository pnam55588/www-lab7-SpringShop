package fit.iuh.lab7.repositories;

import fit.iuh.lab7.models.Product;
import fit.iuh.lab7.models.ProductPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select pr from ProductPrice pr where pr.product.id=:id order by pr.priceDateTime desc limit 1")
    ProductPrice getPrice(@Param("id") long productId);

}