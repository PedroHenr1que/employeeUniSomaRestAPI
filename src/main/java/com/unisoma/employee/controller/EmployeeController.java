package com.unisoma.employee.controller;

import com.unisoma.employee.dto.EmployeeIncomeTaxDTO;
import com.unisoma.employee.dto.EmployeeNewSalaryDTO;
import com.unisoma.employee.exception.EmployeeAlreadyExistException;
import com.unisoma.employee.exception.EmployeeNotFoundException;
import com.unisoma.employee.model.Employee;
import com.unisoma.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

    @Autowired
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // get all employees
    @GetMapping("employees")
    public List<Employee> getAllEmployee() {
        return this.employeeRepository.findAll();
    }

    // create employee
    @PostMapping("employees/register")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) throws EmployeeAlreadyExistException {

        boolean employeeCheck = employeeRepository.findById(employee.getCpf()).isPresent();
        if (employeeCheck) {
            throw new EmployeeAlreadyExistException("Employee with this cpf -> " + employee.getCpf() + " already exist");
        }

        return new ResponseEntity<>(this.employeeRepository.save(employee), HttpStatus.CREATED);
    }

    // update salary employee
    @PutMapping("/employees/newSalary/{cpf}")
    public ResponseEntity<Map<String,String>> updateSalaryEmployee(@PathVariable(value = "cpf") String employeeCpf) throws EmployeeNotFoundException {

        Employee employee = employeeRepository.findById(employeeCpf)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for this cpf :: " + employeeCpf));

        EmployeeNewSalaryDTO employeeDTO = employee.updateEmployeeSalary();
        this.employeeRepository.save(employee);

        DecimalFormat df = new DecimalFormat("0.00");
        Map<String, String> response = new LinkedHashMap<>();
        response.put("CPF",employeeDTO.getCpf());
        response.put("Novo salario",df.format(employeeDTO.getNewSalary()));
        response.put("Reajuste ganho",df.format(employeeDTO.getReadjustment()));
        response.put("Em percentual",(employeeDTO.getPercent() + "%"));


        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // get IT
    @GetMapping("employees/incomeTax/{cpf}")
    public ResponseEntity<Map<String,String>> getIncomeTax(@PathVariable(value = "cpf") String employeeCpf)
            throws EmployeeNotFoundException {

        Employee employee = employeeRepository.findById(employeeCpf)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for this cpf :: " + employeeCpf));


        EmployeeIncomeTaxDTO employeeDTO = employee.calculateIncomeTax();

        DecimalFormat df = new DecimalFormat("0.00");
        Map<String,String> response = new LinkedHashMap<>();
        response.put("CPF",employeeDTO.getCpf());
        response.put(employeeDTO.getIncomeTax() == 0 ? "Isento" : "Imposto",employeeDTO.getIncomeTax() == 0 ? "" : "R$ " + df.format(employeeDTO.getIncomeTax()));

        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
