package fit.iuh.lab7.frontend.controllers;

import fit.iuh.lab7.enums.EmployeeStatus;
import fit.iuh.lab7.enums.ProductStatus;
import fit.iuh.lab7.models.Employee;
import fit.iuh.lab7.models.Product;
import fit.iuh.lab7.models.ProductPrice;
import fit.iuh.lab7.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @RequestMapping("/product-update/{id}")
    public String showUpdatePage(Model model, @PathVariable("id") String id){
        long prodId = Long.parseLong(id);
        Optional<Product> product = productService.findById(prodId);
        model.addAttribute("product", product);
        model.addAttribute("listStatus", ProductStatus.values());
        ProductPrice price = productService.getPrice(prodId);
        model.addAttribute("price", price);
        return "admin/product-update";
    }
    @RequestMapping("/update-product")
    public String showUpdatePage(@ModelAttribute("product") Product product, @ModelAttribute("price") ProductPrice price){
        try {
            productService.save(product);
            price.setPriceDateTime(LocalDateTime.now());
//            System.out.println(price);
            productService.savePrice(price);
            return "redirect:/products-paging";
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }
    @RequestMapping("/product-add")
    public String showAddPage(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("listStatus", ProductStatus.values());
        model.addAttribute("price", new ProductPrice());
        return "admin/product-add";
    }

    @PostMapping("/add-product")
    public String add(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/products-paging";
    }
    @GetMapping("/delete-product/{id}")
    public  String delete(@PathVariable("id") String id){
        long empId = Long.parseLong(id);
        Optional<Product> find = productService.findById(empId);
        Product prod = find.orElse(new Product());
        prod.setStatus(ProductStatus.TERMINATED);
        productService.save(prod);
        return "redirect:/products-paging";
    }

    @GetMapping("/products-paging")
    public String showPaging(Model model,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Product> paging = productService.findAll(currentPage-1,pageSize,"id","desc");
        List<ProductPrice> prices = productService.getPricesForProducts(paging.getContent());
        model.addAttribute("prices", prices);
        model.addAttribute("paging", paging);
        int totalPage = paging.getTotalPages();
        if(totalPage>0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "admin/products";
    }
}
