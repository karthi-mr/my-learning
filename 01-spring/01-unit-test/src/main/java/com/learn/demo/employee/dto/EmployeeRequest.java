package com.learn.demo.employee.dto;

import java.math.BigDecimal;

public record EmployeeRequest(
        String name,

        String email,

        BigDecimal salary
) {
}
