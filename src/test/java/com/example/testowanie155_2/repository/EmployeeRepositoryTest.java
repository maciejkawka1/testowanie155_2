package com.example.testowanie155_2.repository;

import com.example.testowanie155_2.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // domyślnie używa do testowania bazy in-memory, testy są wykonywane w ramach transakcji i po testach jest wykonywany 'roll back'
class EmployeeRepositoryTest {
    // TDD - Test Driven Development - najpierw piszemy test a potem kod aplikacji
    // BDD - behavior driven development, czyli dzielimy test na sekcje
    // given - aktualne wartości, inicjalizacja obiektów
    // when - akcja, testowanie zachowania
    // then - weryfikacja wyniku

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Zapisywanie pracownika")
    void givenEmployee_whenSave_thenReturnSavedEmployee() {
        // given
        Employee employee = new Employee();

        // when
        Employee save = employeeRepository.save(employee);

        // then
        assertThat(save).isNotNull();
        assertThat(save.getId()).isGreaterThan(0);
        assertThat(save).isEqualTo(employee);
    }

    @Test
    @DisplayName("Pobieranie pracowników")
    void givenTwoEmployees_whenFindAll_thenReturnEmployeeList() {
        // given
        Employee employee = new Employee();
        Employee employee2 = new Employee();
        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when
        List<Employee> employees = employeeRepository.findAll();

        // then
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    // pobieranie pracownika po id
    @Test
    @DisplayName("Pobieranie pracownika po id")
    void givenTwoEmployees_whenFindById_thenReturnEmployee() { // można sprawdzić na MySQL
        // given
        Employee employee = new Employee();
        Employee employee2 = new Employee();
        employeeRepository.save(employee);
        employeeRepository.save(employee2);
        System.out.println(employee2);
        // when
        Optional<Employee> byId = employeeRepository.findById(employee2.getId());
        System.out.println(byId.get());

        // then
        assertThat(byId.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Pobieranie pracownika po email")
    void givenTwoEmployees_whenFindByEmail_thenReturnEmployee() {
        // given
        Employee employee = new Employee();
        employee.setEmail("Jarek");
        Employee employee2 = new Employee();
        employee2.setEmail("Oliwier");
        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when
        Optional<Employee> byId = employeeRepository.findByEmail(employee2.getEmail());

        // then
        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get().getEmail()).isEqualTo("Oliwier");
    }

    @Test
    @DisplayName("Uaktualnienie danych pracownika")
    void givenEmployeeWithChangedFields_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given
        Employee employee = new Employee();
        employee.setEmail("Jarek");
        employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("AlaMaKota");
        savedEmployee.setFirstName("Ala");


        // when
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then
        assertThat(updatedEmployee).isNotNull();
    }

    @Test
    @DisplayName("Usuwanie pracownika")
    void givenEmployee_whenDeleteEmployee_thenEmployeeNotPresent() {
        // given
        Employee employee = new Employee();
        employeeRepository.save(employee);

        // when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());

        // then
        assertThat(optionalEmployee).isEmpty();
    }

    @Test
    @DisplayName("Znajdowanie pracownika po imieniu i nazwisku")
    void givenEmployee_whenFindByFirstNameAndLastName_thenReturnEmployee() {
        // given
        Employee employee = new Employee();
        employee.setFirstName("Ala");
        employee.setLastName("Kota");
        employeeRepository.save(employee);

        // when
        Employee foundEmployee = employeeRepository.findByFirstNameAndLastName("Ala", "Kota").get();
        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(employee);
    }

    @Test
    @DisplayName("Znajdowanie pracownika po imieniu i nazwisku")
    void givenEmployee_whenFindByJPQL_thenReturnEmployee() {
        // given
        Employee employee = new Employee();
        employee.setFirstName("Ala");
        employee.setLastName("Kota");
        employeeRepository.save(employee);

        // when
        Employee foundEmployee = employeeRepository.findByJPQL("Ala", "Kota").get();
        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(employee);
    }

    @Test
    @DisplayName("Znajdowanie pracownika po imieniu i nazwisku")
    void givenEmployee_whenFindByJPQLNativeSql_thenReturnEmployee() {
        // given
        Employee employee = new Employee();
        employee.setFirstName("Ala");
        employee.setLastName("Kota");
        employeeRepository.save(employee);

        // when
        Optional<Employee> foundEmployee = employeeRepository.findByJPQLNativeSql("Ala", "Kota");
        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.get()).isEqualTo(employee);
    }

    @Test
    @DisplayName("Znajdowanie pracownika po imieniu i nazwisku")
    void givenEmployee_whenFindByJPQLNativeSqlParam_thenReturnEmployee() {
        // given
        Employee employee = new Employee();
        employee.setFirstName("Ala");
        employee.setLastName("Kota");
        employeeRepository.save(employee);

        // when
        Optional<Employee> foundEmployee = employeeRepository.findByJPQLNativeSqlParam("Ala", "Kota");
        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.get()).isEqualTo(employee);
    }
}