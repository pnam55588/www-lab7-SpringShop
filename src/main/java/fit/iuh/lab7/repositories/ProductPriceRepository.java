package fit.iuh.lab7.repositories;

import fit.iuh.lab7.models.Product;
import fit.iuh.lab7.models.ProductPrice;
import fit.iuh.lab7.pks.ProductPricePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, ProductPricePK> {



    @Query("select pr from ProductPrice pr where (pr.product,pr.priceDateTime) in " +
            "(select pp.product, max(pp.priceDateTime) from ProductPrice pp where pp.product in :products group by pp.product)" +
            "order by pr.product.id desc ")
    List<ProductPrice> findProductPricesByProducts(@Param("products") List<Product> products);
    @Query("select pr from ProductPrice pr where (pr.product,pr.priceDateTime) in " +
            "(select pp.product, max(pp.priceDateTime) from ProductPrice pp where pp.product in :products group by pp.product)" +
            "order by pr.product.id asc ")
    List<ProductPrice> findProductPricesByCart(@Param("products") List<Product> products);
}