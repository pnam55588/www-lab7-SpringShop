package fit.iuh.lab7.repositories;

import fit.iuh.lab7.models.ProductPrice;
import fit.iuh.lab7.pks.ProductPricePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, ProductPricePK> {
}