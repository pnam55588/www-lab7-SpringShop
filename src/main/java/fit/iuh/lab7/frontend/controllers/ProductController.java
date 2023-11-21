package fit.iuh.lab7.frontend.controllers;

import fit.iuh.lab7.enums.ProductStatus;
import fit.iuh.lab7.models.*;
import fit.iuh.lab7.repositories.OrderDetailRepository;
import fit.iuh.lab7.repositories.OrderRepository;
import fit.iuh.lab7.repositories.ProductPriceRepository;
import fit.iuh.lab7.repositories.ProductRepository;
import fit.iuh.lab7.services.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductPriceRepository productPriceRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;


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
    public String add(@ModelAttribute("product") Product product, @ModelAttribute("price") ProductPrice price) {
        productService.save(product);
        price.setProduct(product);
        price.setPriceDateTime(LocalDateTime.now());
        System.out.println(price);
        productPriceRepository.save(price);
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

    @GetMapping("/home")
    public String showProductsUserPage(Model model,
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
        return "user/products";
    }
    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session, @RequestParam("productId") String productId ){
        long id = Long.parseLong(productId);
        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if(cart==null){
            cart = new HashMap<>();
        }
        if(!cart.containsKey(id)){
            cart.put(id,1);
        }
        else {
            cart.put(id,cart.get(id)+1);
        }
        System.out.println("-----------");
        cart.forEach((k,v) -> {System.out.println(k+" : "+v.toString());});
        session.setAttribute("cart",cart);
        return "redirect:/home";
    }
    @GetMapping("/cart")
    public String showCartPage(Model model, HttpSession session){
        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if(cart==null){
            System.out.println("cart is empty");
            return  "user/cart";
        }
        Set<Long> productSet = cart.keySet();
        List<Product> products = productService.findByListId(productSet.stream().toList());
        List<ProductPrice> prices = productService.getPricesForCart(products);
        List<Integer> quantities = cart.values().stream().toList();
        model.addAttribute("prices", prices);
        model.addAttribute("products", products);
        model.addAttribute("quantities", quantities);
        return "user/cart";
    }
    @GetMapping("/remove-from-cart/{id}")
    public String removeItemFromCart(HttpSession session, @PathVariable("id") String productId ){
        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        long id = Long.parseLong(productId);
        cart.remove(id);
        session.setAttribute("cart",cart);
        return "redirect:/cart";
    }
    @GetMapping("/checkout-cart")
    public String checkoutCart(HttpSession session){
        Random random = new Random();
        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        Employee employee = new Employee();
        employee.setId(random.nextInt(99)+1);
        Customer customer = new Customer();
        customer.setId(random.nextInt(99)+1);
        Order order = new Order(LocalDateTime.now(),employee,customer);
        orderRepository.save(order);
        cart.forEach((k,v)->{
            ProductPrice price = productService.getPrice(k);
            Product product = productService.findById(k).orElse(null);
            OrderDetail orderDetail = new OrderDetail(v,price.getPrice(),"",order,product);
            orderDetailRepository.save(orderDetail);
        });
        session.removeAttribute("cart");
        return "redirect:/cart";
    }
}
