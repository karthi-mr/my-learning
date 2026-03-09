package com.learn.demo;

import com.learn.demo.employee.Employee;
import com.learn.demo.employee.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        var employee = Employee.builder()
                .name("John")
                .email("john@mail.com")
                .salary(BigDecimal.valueOf(50_000.00))
                .build();
        this.employeeRepository.save(employee);
    }

    @Test
    void shouldFindEmployeeByEmail() {
        Optional<Employee> found = this.employeeRepository.findByEmail("john@mail.com");

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getName());
        assertEquals("john@mail.com", found.get().getEmail());
    }
}
