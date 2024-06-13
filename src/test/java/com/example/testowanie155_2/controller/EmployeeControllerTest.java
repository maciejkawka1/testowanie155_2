package com.example.testowanie155_2.controller;

import com.example.testowanie155_2.entity.Employee;
import com.example.testowanie155_2.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc; // niezbędne do MVC, czyli by można było testować endpointy

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void givenEmployee_whenSave_ThenReturnEmployee() throws Exception {
        // given
        BDDMockito.given(employeeService.save(any(Employee.class)))
//                .willAnswer(invocation -> invocation.getArgument(0)); // to samo co poniżej, ale z odpowiedzią nie obiektem
                .willReturn(employee);
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
    public void x() throws Exception {
        // given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(new Employee());
        BDDMockito.given(employeeService.findAll()).willReturn(employeeList);

        // when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",
                        is(employeeList.size())));
    }

}