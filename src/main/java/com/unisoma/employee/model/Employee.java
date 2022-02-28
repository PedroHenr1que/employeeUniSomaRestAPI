package com.unisoma.employee.model;

import com.unisoma.employee.dto.EmployeeIncomeTaxDTO;
import com.unisoma.employee.dto.EmployeeNewSalaryDTO;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
    //Nome, CPF, Data de Nascimento, Telefone, Endereço e Salario.
    @Id
    @Column(unique = true)
    private String cpf;

    @Column(name = "name")
    private String name;

    @Column(name = "bornDate")
    private String bornDate;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "salary")
    private double salary;

    public Employee() {
    }

    public Employee(String cpf, String name, String bornDate, String phone, String address, double salary) {

        this.cpf = cpf;
        this.name = name;
        this.bornDate = bornDate;
        this.phone = phone;
        this.address = address;

        if (salary <= 0) {
            this.salary = 0;
        } else {
            this.salary = salary;
        }

    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public String getBornDate() {
        return bornDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public EmployeeNewSalaryDTO updateEmployeeSalary() {
        double newSalary;
        double currentSalary = this.salary;
        int percent;
        double readjustment;

        if (currentSalary <= 400.00) {
            percent = 15;
        } else if (currentSalary <= 800.00) {
            percent = 12;
        } else if (currentSalary <= 1200.00) {
            percent = 10;
        } else if (currentSalary <= 2000.00) {
            percent = 7;
        } else {
            percent = 4;
        }

        readjustment = currentSalary * (percent/100.0);
        newSalary = currentSalary + readjustment;
        this.setSalary(newSalary);

        return new EmployeeNewSalaryDTO(this.cpf,this.salary,readjustment, percent);
    }

    public EmployeeIncomeTaxDTO calculateIncomeTax() {
        double salary = this.getSalary();
        double incomeTax = 0;

        if (salary > 2000.00 && salary <= 3000.00) {
            incomeTax = (salary - 2000) * 0.08;

        } else if (salary > 2000.00 && salary <= 4500.00) {
            incomeTax = 1000 *  0.08;
            salary -= 3000;
            incomeTax += salary * 0.18;

        } else if (salary > 4500.00){
            incomeTax = 1000 * 0.08;
            incomeTax += 1500 * 0.18;
            salary -= 4500;
            incomeTax += salary * 0.28;

        }

        return new EmployeeIncomeTaxDTO(incomeTax,this.cpf);
    }
}
