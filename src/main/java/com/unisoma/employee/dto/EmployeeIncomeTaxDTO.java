package com.unisoma.employee.dto;

public class EmployeeIncomeTaxDTO {
    private double incomeTax;
    private String cpf;

    public EmployeeIncomeTaxDTO(double incomeTax, String cpf) {
        this.incomeTax = incomeTax;
        this.cpf = cpf;
    }

    public double getIncomeTax() {
        return incomeTax;
    }

    public String getCpf() {
        return cpf;
    }
}
