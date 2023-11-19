package fit.iuh.lab7.services;

import fit.iuh.lab7.models.Product;
import fit.iuh.lab7.models.ProductPrice;
import fit.iuh.lab7.repositories.ProductPriceRepository;
import fit.iuh.lab7.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductPriceRepository productPriceRepository;

    public Optional<Product> findById(long id){return  productRepository.findById(id);};
    public Page<Product> findAll(int page, int size, String orderBy, String softDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(softDirection),orderBy);
        Pageable pageable = PageRequest.of(page,size,sort);
        return productRepository.findAll(pageable);
    }
    public ProductPrice getPrice(long productId){return productRepository.getPrice(productId);}
    public List<ProductPrice> getPricesForProducts(List<Product> products) {
        return productPriceRepository.findProductPricesByProducts(products);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(long id) {
        productRepository.deleteById(id);
    }

    public ProductPrice savePrice(ProductPrice price) {
        return productPriceRepository.save(price);
    }
}
