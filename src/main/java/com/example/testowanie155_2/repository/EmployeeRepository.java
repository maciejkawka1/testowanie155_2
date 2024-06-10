package com.example.testowanie155_2.repository;

import com.example.testowanie155_2.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
