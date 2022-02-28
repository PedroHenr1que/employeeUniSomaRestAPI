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

    /*// get employee by cpf
    @GetMapping("employees/{cpf}")
    public ResponseEntity<Employee> getEmployeeByCpf(@PathVariable(value = "cpf") String employeeCpf)
            throws EmployeeNotFoundException {

        Employee employee = employeeRepository.findById(employeeCpf)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for this cpf :: " + employeeCpf));
        return ResponseEntity.ok().body(employee);
    }*/

    // save employee
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

        Map<String, String> response = new LinkedHashMap<>();
        response.put("CPF",employeeDTO.getCpf());
        response.put("Novo salario",String.valueOf(employeeDTO.getNewSalary()));
        response.put("Reajuste ganho",String.valueOf(employeeDTO.getReadjustment()));
        response.put("Em percentual",(employeeDTO.getPercent() + "%"));


        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // get IR
    @GetMapping("employees/incomeTax/{cpf}")
    public ResponseEntity<Map<String,String>> getIncomeTax(@PathVariable(value = "cpf") String employeeCpf)
            throws EmployeeNotFoundException {

        Employee employee = employeeRepository.findById(employeeCpf)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for this cpf :: " + employeeCpf));


        EmployeeIncomeTaxDTO employeeDTO = employee.calculateIncomeTax();

        Map<String,String> response = new LinkedHashMap<>();
        response.put("CPF",employeeDTO.getCpf());
        response.put(employeeDTO.getIncomeTax() == 0 ? "Isento" : "Imposto",employeeDTO.getIncomeTax() == 0 ? "" : "R$ " + employeeDTO.getIncomeTax());

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /*// delete employee
    @DeleteMapping("employees/delete/{cpf}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "cpf") String employeeCpf) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(employeeCpf)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for this cpf :: " + employeeCpf));

        this.employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }*/
}
