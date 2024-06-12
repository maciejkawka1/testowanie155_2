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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee(
                1L,
                "Karol",
                "Marks",
                "karol@marks"
        );
    }

    @Test
    public void givenEmployee_whenSaveEmployee_thenReturnEmployee() {
        // given
        BDDMockito.given(employeeRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when
        Employee save = employeeService.save(employee);

        // then
        assertThat(save).isEqualTo(employee);
    }

    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowException() {
        // given
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when
        assertThrows(RuntimeException.class, () -> employeeService.save(employee));

        // then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {
        // given
        Employee employee2 = new Employee(
                2L,
                "Karol",
                "Marks",
                "karol@marks");
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));
        // when
        List<Employee> employeeList = employeeService.findAll();

        // then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    public void givenEmployeeList_whenFindAll_thenReturnEmptyEmployeeList() {
        // given
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        // when
        List<Employee> employeeList = employeeService.findAll();

        // then
        assertThat(employeeList).isEmpty();
    }

    @Test
    public void givenEmployeeId_whenFindById_thenReturnEmployee(){
        // given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when
        Optional<Employee> optionalEmployee = employeeService.findById(1L);

        // then
        assertThat(optionalEmployee).isPresent();
        assertThat(optionalEmployee.get()).isEqualTo(employee);
    }

    @Test
    public void givenEmployee_whenDeleteEmployee_thenDoNothing() {
        // given
        willDoNothing().given(employeeRepository).deleteById(anyLong());
        // when
        employeeService.delete(1L);

        // then
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }
}