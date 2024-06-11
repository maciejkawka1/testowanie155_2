package com.example.testowanie155_2.repository;

import com.example.testowanie155_2.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email); //JPA - zamieni nazwę metody na zapytanie SQL

    Employee findByFirstNameAndLastName(String firstName, String lastName);
}
