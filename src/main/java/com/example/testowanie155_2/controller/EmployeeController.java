package com.example.testowanie155_2.controller;

import com.example.testowanie155_2.entity.Employee;
import com.example.testowanie155_2.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee save(@RequestBody Employee employee){
        return employeeService.save(employee);
    }

    @GetMapping
    public List<Employee> findAll(){
        return employeeService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> findById(@PathVariable long id){
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
