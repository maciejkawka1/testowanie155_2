package com.example.testowanie155_2.service;

import com.example.testowanie155_2.entity.Employee;
import com.example.testowanie155_2.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent())
            throw new RuntimeException("Employee already exists with given email:" + employee.getEmail());
        return employeeRepository.save(employee);
    }
}
