package com.javaWebProject.ecomproj.service;

import com.javaWebProject.ecomproj.model.Employee;
import com.javaWebProject.ecomproj.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setName(updatedEmployee.getName());
            employee.setEmail(updatedEmployee.getEmail());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new IllegalArgumentException("Employee with ID " + id + " not found"));
    }

    public void deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Employee with ID " + id + " not found");
        }
    }
}
