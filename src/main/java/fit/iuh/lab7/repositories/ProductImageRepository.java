package fit.iuh.lab7.repositories;

import fit.iuh.lab7.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}