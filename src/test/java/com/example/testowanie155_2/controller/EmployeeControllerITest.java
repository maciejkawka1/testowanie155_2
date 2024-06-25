package com.example.testowanie155_2.controller;

import com.example.testowanie155_2.entity.Employee;
import com.example.testowanie155_2.repository.EmployeeRepository;
import com.example.testowanie155_2.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // uruchamiamy serwer na
@AutoConfigureMockMvc
class EmployeeControllerITest {

    @Autowired
    private MockMvc mockMvc; // niezbędne do MVC, czyli by można było testować endpointy

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll();
        employee = new Employee(
                1L,
                "Karol",
                "Marks",
                "karol@marks"
        );
    }

    @Test
    public void givenEmployee_whenSave_ThenReturnEmployee() throws Exception {
        // given
        // when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    @Test
    public void givenListOfEmployees_whenFindAll_thenReturnEmployeeListAndStatusOk() throws Exception {
        // given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(new Employee());
        employeeRepository.saveAll(employeeList);

        // when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",
                        is(employeeList.size())));
    }


    @Test
    public void givenEmployeeId_whenFindById_thenReturnEmployee() throws Exception {
        // given
        employeeRepository.save(employee);
        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 1L));
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    @Test
    public void givenEmployeeId_whenFindById_thenReturnNotFound() throws Exception {
        // given
        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 1L));
        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenEmployeeIdAndUpdatedEmployee_whenUpdateEmploy_thenReturnUpdatedEmployee() throws Exception {
        // given

        Employee save = employeeRepository.save(employee);
        Employee updatedEmployee = new Employee(
                save.getId(),
                "Konrad",
                "Walenrod",
                "konrad@marks"
        );
        // when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", save.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(updatedEmployee.getEmail())));
    }

    @Test
    public void givenEmployeeIdAndUpdatedEmployee_whenUpdateEmploy_thenReturnStatusNotFound() throws Exception {
        // given
        Employee updatedEmployee = new Employee(
                1L,
                "Konrad",
                "Walenrod",
                "konrad@marks"
        );

        // when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenEmployeeId_whenDelete_thenReturnOk() throws Exception {
        // given
        employeeRepository.save(employee);

        // when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", 1L));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }

}