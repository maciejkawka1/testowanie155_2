package com.example.testowanie155_2.service;

import com.example.testowanie155_2.entity.Employee;
import com.example.testowanie155_2.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        employee = new Employee(
                1L,
                "Karol",
                "Marks",
                "karol@marks"
        );
    }

    @Test
    public void givenEmployee_whenSaveEmployee_thenReturnEmployee(){
        // given
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when
        Employee save = employeeService.save(employee);

        // then
        assertThat(save).isEqualTo(employee);
    }
}