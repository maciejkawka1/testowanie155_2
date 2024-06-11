package com.example.testowanie155_2.repository;

import com.example.testowanie155_2.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email); //JPA - zamieni nazwę metody na zapytanie SQL

    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("select e from Employee as e where e.firstName = ?1 and e.lastName =?2")
    Optional<Employee> findByJPQL(String firstName, String lastName);

    @Query(value = "select * from employees as e where e.first_name = ?1 and e.last_name =?2"
            , nativeQuery = true)
    Optional<Employee> findByJPQLNativeSql(String firstName, String lastName);

    @Query(value = "select * from employees as e where e.first_name =:name and e.last_name =:lastName"
            , nativeQuery = true)
    Optional<Employee> findByJPQLNativeSqlParam(@Param("name") String firstName, String lastName);
}
