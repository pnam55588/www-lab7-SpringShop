package fit.iuh.lab7.frontend.controllers;

import fit.iuh.lab7.models.Customer;
import fit.iuh.lab7.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;


    @RequestMapping("/customer-update/{id}")
    public String showUpdatePage(Model model, @PathVariable("id") String id){
        long custId = Long.parseLong(id);
        Optional<Customer> customer = customerService.findById(custId);
        model.addAttribute("customer", customer);
        return "admin/customer-update";
    }
    @RequestMapping("/update-customer")
    public String showUpdatePage(@ModelAttribute("customer") Customer customer){
        try {
            customerService.save(customer);
            System.out.println(customer);
            return "redirect:/customers-paging";
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }
    @RequestMapping("/customer-add")
    public String showAddPage(Model model){
        model.addAttribute("customer", new Customer());
        return "admin/customer-add";
    }

    @PostMapping("/add-customer")
    public String add(@ModelAttribute("customer") Customer customer) {
        customerService.save(customer);
        return "redirect:/customers-paging";
    }
    @GetMapping("/delete-customer/{id}")
    public  String delete(@PathVariable("id") String id){
        long custId = Long.parseLong(id);
        customerService.delete(custId);
        return "redirect:/customers-paging";
    }

    @GetMapping("/customers-paging")
    public String showPaging(Model model,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Customer> paging = customerService.findAll(currentPage-1,pageSize,"id","desc");
        model.addAttribute("postPage", paging);
        int totalPage = paging.getTotalPages();
        if(totalPage>0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "admin/customers";
    }
}
