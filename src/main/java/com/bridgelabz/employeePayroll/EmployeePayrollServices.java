package com.bridgelabz.employeePayroll;

import java.util.List;

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

}
