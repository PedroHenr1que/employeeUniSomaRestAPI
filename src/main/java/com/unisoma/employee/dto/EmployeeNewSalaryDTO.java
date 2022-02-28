package com.unisoma.employee.dto;

public class EmployeeNewSalaryDTO {

    private String cpf;
    private double newSalary;
    private double readjustment;
    private int percent;

    public EmployeeNewSalaryDTO(String cpf, double newSalary, double readjustment, int percent) {
        this.cpf = cpf;
        this.newSalary = newSalary;
        this.readjustment = readjustment;
        this.percent = percent;
    }

    public String getCpf() {
        return cpf;
    }

    public double getNewSalary() {
        return newSalary;
    }

    public double getReadjustment() {
        return readjustment;
    }

    public int getPercent() {
        return percent;
    }
}
