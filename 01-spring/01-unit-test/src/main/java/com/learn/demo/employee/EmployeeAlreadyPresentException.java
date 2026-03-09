package com.learn.demo.employee;

public class EmployeeAlreadyPresentException extends RuntimeException {

    public EmployeeAlreadyPresentException(String email) {
        super("Employee already present with this email: " + email);
    }
}
