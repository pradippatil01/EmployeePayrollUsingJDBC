package com.bridgelabz.employeePayroll;

import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollServices {

    public List<EmployeePayrollData> employeePayrollList;
    public EmployeePayrollDBServices employeePayrollDBServices;


    public enum IOService {
        DB_IO
    }

    public EmployeePayrollServices() {
        employeePayrollDBServices = EmployeePayrollDBServices.getInstance();
    }

     public EmployeePayrollServices(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    public List<EmployeePayrollData> readEmployeeData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBServices.readData();
        return this.employeePayrollList;
    }

    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBServices.updateData(name, salary);
        if (result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeeData(name);
        if (employeePayrollData != null)
            employeePayrollData.salary = salary;
    }

    public boolean checkEmployeeDataSyncWithDB(String name) {
        List<EmployeePayrollData> employeeDataList = employeePayrollDBServices.getEmployeePayrollData(name);
        return employeeDataList.get(0).equals(getEmployeeData(name));

    }

    public List<EmployeePayrollData> readEmployeeDataForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDBServices.getEmployeeForDateRange(startDate, endDate);
        return null;
    }

    private EmployeePayrollData getEmployeeData(String name) {
        EmployeePayrollData employeePayrollData;
        employeePayrollData = this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
        return employeePayrollData;
    }

    public Map<String, Double> readAverageSalaryByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDBServices.getAverageSalaryByGender();
        return null;
    }

    public void addEmployeeToPayroll(String name, double salary, LocalDate date, String gender) {
    employeePayrollList.add(employeePayrollDBServices.addEmployeeToPayroll(name, salary, date, gender));
    }

}
