package com.learn.demo.employee;

import com.learn.demo.employee.dto.EmployeeRequest;
import com.learn.demo.employee.dto.EmployeeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> saveEmployee(@RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.employeeService.saveEmployee(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.employeeService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<EmployeeResponse>> findById() {
        return ResponseEntity.ok(this.employeeService.findAll());
    }
}
