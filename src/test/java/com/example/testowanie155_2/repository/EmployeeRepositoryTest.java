package com.example.testowanie155_2.repository;

import com.example.testowanie155_2.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    void x() {
        // given
        Employee employee = new Employee();
        // when
        Employee save = employeeRepository.save(employee);

        // then
        assertThat(save).isNotNull();
    }
}