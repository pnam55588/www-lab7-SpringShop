package fit.iuh.lab7.frontend.controllers;

import fit.iuh.lab7.enums.EmployeeStatus;
import fit.iuh.lab7.models.Customer;
import fit.iuh.lab7.models.Employee;
import fit.iuh.lab7.services.CustomerService;
import fit.iuh.lab7.services.EmployeeService;
import lombok.RequiredArgsConstructor;
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
public class EmployeeController {

    private final EmployeeService employeeService;

    @RequestMapping("/employee-update/{id}")
    public String showUpdatePage(Model model, @PathVariable("id") String id){
        long empId = Long.parseLong(id);
        Optional<Employee> employee = employeeService.findById(empId);
        model.addAttribute("employee", employee);
        model.addAttribute("listStatus", EmployeeStatus.values());
        return "admin/employee-update";
    }
    @RequestMapping("/update-employee")
    public String showUpdatePage(@ModelAttribute("employee") Employee employee){
        try {
            employeeService.save(employee);
            return "redirect:/employees-paging";
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }
    @RequestMapping("/employee-add")
    public String showAddPage(Model model){
        model.addAttribute("employee", new Employee());
        model.addAttribute("listStatus", EmployeeStatus.values());
        return "admin/employee-add";
    }

    @PostMapping("/add-employee")
    public String add(@ModelAttribute("employee") Employee employee) {
        employeeService.save(employee);
        return "redirect:/employees-paging";
    }
    @GetMapping("/delete-employee/{id}")
    public  String delete(@PathVariable("id") String id){
        long empId = Long.parseLong(id);
        Optional<Employee> findEmp = employeeService.findById(empId);
        Employee emp = findEmp.orElse(new Employee());
        emp.setStatus(EmployeeStatus.TERMINATED);
        employeeService.save(emp);
        return "redirect:/employees-paging";
    }

    @GetMapping("/employees-paging")
    public String showPaging(Model model,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Employee> paging = employeeService.findAll(currentPage-1,pageSize,"id","desc");
        model.addAttribute("paging", paging);
        int totalPage = paging.getTotalPages();
        if(totalPage>0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "admin/employees";
    }
}
