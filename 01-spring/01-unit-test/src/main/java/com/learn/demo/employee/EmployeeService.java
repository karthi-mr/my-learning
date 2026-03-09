package com.learn.demo.employee;

import com.learn.demo.employee.dto.EmployeeRequest;
import com.learn.demo.employee.dto.EmployeeResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @NonNull
    public EmployeeResponse saveEmployee(@NonNull EmployeeRequest request) {
        Optional<Employee> employee = this.employeeRepository.findByEmail(request.email());

        if (employee.isPresent()) {
            throw new EmployeeAlreadyPresentException(request.email());
        }

        var savedEmployee = this.employeeRepository.save(this.employeeMapper.toEmployee(request));
        return this.employeeMapper.toEmployeeResponse(savedEmployee);
    }

    @NonNull
    public EmployeeResponse findById(@NonNull Long id) {
        return this.employeeRepository.findById(id)
                .map(this.employeeMapper::toEmployeeResponse)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for id: " + id));
    }

    @NonNull
    public List<EmployeeResponse> findAll() {
        return this.employeeRepository.findAll().stream()
                .map(this.employeeMapper::toEmployeeResponse)
                .toList();
    }

    public void deleteById(@NonNull Long id) {
        this.employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for id: " + id));
        this.employeeRepository.deleteById(id);
    }
}
