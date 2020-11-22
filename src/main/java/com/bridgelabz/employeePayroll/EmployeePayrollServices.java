package com.bridgelabz.employeePayroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EmployeePayrollServices {
    public List<EmployeePayrollData> employeePayrollList;
    public EmployeePayrollDBServices employeePayrollDBServices;


    public enum IOService {
        DB_IO
    }

    public EmployeePayrollServices() {
        employeePayrollDBServices = EmployeePayrollDBServices.getInstance();
    }

    public List<EmployeePayrollData> readEmployeeData(IOService ioService) throws InvalidException {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBServices.readData();
        return this.employeePayrollList;
    }

    public List<EmployeePayrollData> addEmployeeToPayroll(String name, double salary, LocalDate date, String gender, IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = (List<EmployeePayrollData>) employeePayrollDBServices.addEmployeeToPayroll(name,salary,date,gender);
        return this.employeePayrollList;
    }

    public void updateEmployeeSalary(String name, double salary) throws InvalidException {
        int result = employeePayrollDBServices.updateData(name, salary);
        if (result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeeData(name);
        if (employeePayrollData != null)
            employeePayrollData.salary = salary;
    }

    private EmployeePayrollData getEmployeeData(String name) {
        EmployeePayrollData employeePayrollData;
        employeePayrollData = this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
        return employeePayrollData;
    }

    public boolean checkEmployeeDataSyncWithDB(String name) {
        List<EmployeePayrollData> employeeDataList = employeePayrollDBServices.getEmployeePayrollData(name);
        return employeeDataList.get(0).equals(getEmployeeData(name));

    }

    public List<EmployeePayrollData> readEmployeeDataForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) throws InvalidException {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDBServices.getEmployeeForDateRange(startDate, endDate);
        return null;
    }

    public Map<String, Double> readAverageSalaryByGender(IOService ioService) throws InvalidException {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDBServices.getAverageSalaryByGender();
        return null;
    }
}
