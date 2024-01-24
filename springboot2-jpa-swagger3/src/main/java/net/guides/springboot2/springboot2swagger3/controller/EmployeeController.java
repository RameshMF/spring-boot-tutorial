package net.guides.springboot2.springboot2swagger3.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.springboot2swagger3.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2swagger3.model.Employee;
import net.guides.springboot2.springboot2swagger3.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1")
@Tag(
    name = "Employee Management System",
    description = "Operations pertaining to employee in Employee Management System")
public class EmployeeController {
  @Autowired private EmployeeRepository employeeRepository;

  @Operation(summary = "View a list of available employees")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(
            responseCode = "401",
            description = "You are not authorized to view the resource"),
        @ApiResponse(
            responseCode = "403",
            description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(
            responseCode = "404",
            description = "The resource you were trying to reach is not found")
      })
  @GetMapping("/employees")
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  @Operation(summary = "Get an employee by Id")
  @GetMapping("/employees/{id}")
  public ResponseEntity<Employee> getEmployeeById(
      @Parameter(
              description = "Employee id from which employee object will retrieve",
              required = true,
              in = ParameterIn.PATH)
          @PathVariable(value = "id")
          Long employeeId)
      throws ResourceNotFoundException {
    Employee employee =
        employeeRepository
            .findById(employeeId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Employee not found for this id :: " + employeeId));
    return ResponseEntity.ok().body(employee);
  }

  @Operation(summary = "Add an employee")
  @PostMapping("/employees")
  public Employee createEmployee(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Employee object store in database table",
              required = true)
          @Valid
          @RequestBody
          Employee employee) {
    return employeeRepository.save(employee);
  }

  @Operation(summary = "Update an employee")
  @PutMapping("/employees/{id}")
  public ResponseEntity<Employee> updateEmployee(
      @Parameter(
              description = "Employee Id to update employee object",
              required = true,
              in = ParameterIn.PATH)
          @PathVariable(value = "id")
          Long employeeId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Update employee object",
              required = true)
          @Valid
          @RequestBody
          Employee employeeDetails)
      throws ResourceNotFoundException {
    Employee employee =
        employeeRepository
            .findById(employeeId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Employee not found for this id :: " + employeeId));

    employee.setEmailId(employeeDetails.getEmailId());
    employee.setLastName(employeeDetails.getLastName());
    employee.setFirstName(employeeDetails.getFirstName());
    final Employee updatedEmployee = employeeRepository.save(employee);
    return ResponseEntity.ok(updatedEmployee);
  }

  @Operation(summary = "Delete an employee")
  @DeleteMapping("/employees/{id}")
  public Map<String, Boolean> deleteEmployee(
      @Parameter(
              description =
                  "Employee Id from which employee object will delete from database table",
              required = true,
              in = ParameterIn.PATH)
          @PathVariable(value = "id")
          Long employeeId)
      throws ResourceNotFoundException {
    Employee employee =
        employeeRepository
            .findById(employeeId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Employee not found for this id :: " + employeeId));

    employeeRepository.delete(employee);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
}
