package com.learn.demo;

import com.learn.demo.employee.*;
import com.learn.demo.employee.dto.EmployeeRequest;
import com.learn.demo.employee.dto.EmployeeResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// @SpringBootTest
@ExtendWith(MockitoExtension.class)
@Slf4j
class ApplicationTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeResponse employeeResponseDto;
    private EmployeeRequest employeeRequest;
    private Employee employee;

    @BeforeEach
    void setup() {
        this.employee = Employee.builder()
                .id(1L)
                .name("John")
                .email("john@mail.com")
                .salary(BigDecimal.valueOf(50_000.00))
                .build();
        this.employeeResponseDto = new EmployeeResponse(
                1L,
                "John",
                "john@mail.com",
                BigDecimal.valueOf(50_000.00)
        );
        this.employeeRequest = new EmployeeRequest(
                "John",
                "john@mail.com",
                BigDecimal.valueOf(50_000.00)
        );
    }


    @Test
    void shouldSaveEmployeeSuccessfully() {
        when(this.employeeRepository.findByEmail(this.employee.getEmail()))
                .thenReturn(Optional.empty());
        when(this.employeeRepository.save(this.employee))
                .thenReturn(this.employee);
        when(this.employeeMapper.toEmployeeResponse(this.employee))
                .thenReturn(this.employeeResponseDto);
        when(this.employeeMapper.toEmployee(this.employeeRequest))
                .thenReturn(this.employee);

        EmployeeResponse employeeResponse = this.employeeService.saveEmployee(this.employeeRequest);

        assertNotNull(employeeResponse);
        assertEquals("John", employeeResponse.name());
        assertEquals("john@mail.com", employeeResponse.email());

        verify(this.employeeRepository, times(1)).findByEmail(this.employee.getEmail());
        verify(this.employeeRepository, times(1)).save(this.employee);
        verify(this.employeeMapper, times(1)).toEmployeeResponse(this.employee);
        verify(this.employeeMapper, times(1)).toEmployee(this.employeeRequest);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeAlreadyExists() {
        when(this.employeeRepository.findByEmail(this.employee.getEmail()))
                .thenReturn(Optional.of(this.employee));
        EmployeeAlreadyPresentException exception = assertThrows(EmployeeAlreadyPresentException.class, () ->
                this.employeeService.saveEmployee(this.employeeRequest));
        assertEquals("Employee already present with this email: john@mail.com", exception.getMessage());

        verify(this.employeeRepository, times(1)).findByEmail(this.employee.getEmail());
        verify(this.employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldReturnEmployeeById() {
        when(this.employeeRepository.findById(1L))
                .thenReturn(Optional.of(this.employee));

        when(this.employeeMapper.toEmployeeResponse(this.employee))
                .thenReturn(this.employeeResponseDto);

        EmployeeResponse employeeResponse = this.employeeService.findById(1L);

        assertNotNull(employeeResponse);
        assertEquals("John", employeeResponse.name());
        assertEquals("john@mail.com", employeeResponse.email());

        verify(this.employeeRepository, times(1)).findById(1L);
        verify(this.employeeMapper, times(1)).toEmployeeResponse(this.employee);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {
        when(this.employeeRepository.findById(1L))
                .thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
            this.employeeService.findById(1L));
        assertEquals("Employee not found for id: " + 1L, exception.getMessage());
        verify(this.employeeRepository, times(1)).findById(1L);
        verify(this.employeeMapper, never()).toEmployeeResponse(any(Employee.class));
    }

    @Test
    void shouldReturnAllEmployees() {
        Employee employee2 = Employee.builder()
                .id(2L)
                .name("Kiran")
                .email("kiran@mail.com")
                .salary(BigDecimal.valueOf(30_000.00))
                .build();

        when(this.employeeRepository.findAll())
                .thenReturn(List.of(this.employee, employee2));
        when(this.employeeMapper.toEmployeeResponse(this.employee))
                .thenReturn(this.employeeResponseDto);
        when(this.employeeMapper.toEmployeeResponse(employee2))
                .thenReturn(new EmployeeResponse(2L, "Kiran", "kiran@mail.com", BigDecimal.valueOf(30_000.00)));

        List<EmployeeResponse> employeeResponses = this.employeeService.findAll();
        assertEquals(2, employeeResponses.size());
        assertEquals("John", employeeResponses.get(0).name());
        assertEquals("Kiran", employeeResponses.get(1).name());

        verify(this.employeeRepository, times(1)).findAll();
        verify(this.employeeMapper, times(2)).toEmployeeResponse(any(Employee.class));
    }

    @Test
    void shouldDeleteEmployeeSuccessfully() {
        when(this.employeeRepository.findById(1L))
                .thenReturn(Optional.of(this.employee));
        doNothing().when(this.employeeRepository).deleteById(1L);

        this.employeeService.deleteById(1L);

        verify(this.employeeRepository, times(1)).findById(1L);
        verify(this.employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingEmployee() {
        when(this.employeeRepository.findById(1L))
                .thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
            this.employeeService.findById(1L));
        assertEquals("Employee not found for id: " + 1L, exception.getMessage());
        verify(this.employeeRepository, times(1)).findById(1L);
        verify(this.employeeRepository, never()).deleteById(1L);


    }
}
