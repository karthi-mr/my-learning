package com.learn.demo;

import com.learn.demo.employee.Employee;
import com.learn.demo.employee.EmployeeController;
import com.learn.demo.employee.EmployeeService;
import com.learn.demo.employee.dto.EmployeeRequest;
import com.learn.demo.employee.dto.EmployeeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private EmployeeRequest employeeRequest;
    private EmployeeResponse employeeResponse;

    @BeforeEach
    void setup() {
        this.employee = Employee.builder()
                .id(1L)
                .name("John")
                .email("john@mail.com")
                .salary(BigDecimal.valueOf(50_000.00))
                .build();
        this.employeeRequest = new EmployeeRequest("John", "john@mail.com", BigDecimal.valueOf(50_000));
        this.employeeResponse = new EmployeeResponse(1L, "John", "john@mail.com", BigDecimal.valueOf(50_000));
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        when(this.employeeService.saveEmployee(any(EmployeeRequest.class)))
                .thenReturn(this.employeeResponse);

        this.mockMvc.perform(
                post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.employeeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@mail.com"));
    }
}
