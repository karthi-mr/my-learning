package com.learn.demo.employee;

import com.learn.demo.employee.dto.EmployeeRequest;
import com.learn.demo.employee.dto.EmployeeResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    @NonNull
    public Employee toEmployee(@NonNull EmployeeRequest request) {
        return Employee.builder()
                .name(request.name())
                .email(request.email())
                .salary(request.salary())
                .build();
    }

    @NonNull
    public EmployeeResponse toEmployeeResponse(@NonNull Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary()
        );
    }
}
