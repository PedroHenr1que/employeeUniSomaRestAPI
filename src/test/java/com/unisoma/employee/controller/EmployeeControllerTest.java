package com.unisoma.employee.controller;

import com.unisoma.employee.exception.EmployeeAlreadyExistException;
import com.unisoma.employee.exception.EmployeeNotFoundException;
import com.unisoma.employee.model.Employee;
import com.unisoma.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class EmployeeControllerTest {

    private EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);

    private EmployeeController employeeController = new EmployeeController(employeeRepository);

    Employee employee1 = new Employee("12334234561", "Pedro", "14/02/2003", "81956345788", "Recife-PE", 5000);
    Employee employee2 = new Employee("64758194782", "Alvaro", "10/01/1972", "82923446614", "Rio de Janeiro-RJ", 1000);
    Employee employee3 = new Employee("16483927495", "Renata", "05/04/1970", "83933667823", "São Paulo-SP", 3002);
    Employee employee4 = new Employee("25671847307","Tamyris","11/12/1992","11933445623","Garanhuns-PE",4000);
    /*@Test
    void getAllEmployee() {

        List<Employee> employees = new ArrayList<>(Arrays.asList(employee1, employee2, employee3));

        Mockito.when(employeeController.getAllEmployee()).thenReturn(employees);

        List<Employee> actualEmployees = employeeController.getAllEmployee();

        Assertions.assertEquals(employees.size(),actualEmployees.size());
    }*/

    /*@Test
    void getEmployeeByCpfTest() throws EmployeeNotFoundException {

        Mockito.when(employeeRepository.findById(employee1.getCpf())).thenReturn(Optional.of(employee1));

        Employee actualEmployee = employeeController.getEmployeeByCpf(employee1.getCpf()).getBody();

        assert actualEmployee != null;
        Assertions.assertEquals(employee1.getCpf(),actualEmployee.getCpf());
    }*/

    @Test
    void updateSalaryTest() throws EmployeeNotFoundException {


        Map<String, String> response = new LinkedHashMap<>();
        response.put("CPF",employee2.getCpf());
        response.put("Novo salario",String.valueOf(employee2.getSalary() + ( employee2.getSalary() * 0.10)));
        response.put("Reajuste ganho",String.valueOf(employee2.getSalary() * 0.10));
        response.put("Em percentual", "10%");


        Mockito.when(employeeRepository.findById(employee2.getCpf())).thenReturn(Optional.of(employee2));
        Mockito.when(employeeRepository.save(employee2)).thenReturn(employee2);

        ResponseEntity<Map<String,String>> responseEntity = employeeController.updateSalaryEmployee(employee2.getCpf());

        Assertions.assertEquals(response.get("CPF"),responseEntity.getBody().get("CPF"));
        Assertions.assertEquals(response.get("Novo salario"),responseEntity.getBody().get("Novo salario"));
        Assertions.assertEquals(response.get("Reajuste ganho"),responseEntity.getBody().get("Reajuste ganho"));
        Assertions.assertEquals(response.get("Em percentual"),responseEntity.getBody().get("Em percentual"));

    }

    @Test
    void getIncomeTaxTest() throws EmployeeNotFoundException {


        Map<String,String> response = new LinkedHashMap<>();
        response.put("CPF",employee3.getCpf());
        response.put(18 == 0 ? "Isento" : "Imposto",18 == 0 ? "" : "R$ " + 80.36);

        Mockito.when(employeeRepository.findById(employee3.getCpf())).thenReturn(Optional.of(employee3));

        ResponseEntity<Map<String,String>> actualResponse = employeeController.getIncomeTax(employee3.getCpf());

        Assertions.assertEquals(response.get("CPF"),actualResponse.getBody().get("CPF"));
        Assertions.assertEquals(response.get("Imposto"),actualResponse.getBody().get("Imposto"));

    }

    @Test
    void createEmployee() throws EmployeeAlreadyExistException {

        Mockito.when(employeeRepository.findById(employee4.getCpf())).thenReturn(Optional.empty());
        Mockito.when(employeeRepository.save(employee4)).thenReturn(employee4);

        ResponseEntity<Employee> response = employeeController.createEmployee(employee4);

        Assertions.assertEquals(employee4.getCpf(),response.getBody().getCpf());
        Assertions.assertEquals(employee4.getSalary(),response.getBody().getSalary());
        Assertions.assertEquals(employee4.getAddress(),response.getBody().getAddress());
        Assertions.assertEquals(employee4.getBornDate(),response.getBody().getBornDate());
        Assertions.assertEquals(employee4.getName(),response.getBody().getName());
        Assertions.assertEquals(employee4.getPhone(),response.getBody().getPhone());
    }
}
