package com.bridgelabz.employeePayroll;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.employeePayroll.EmployeePayrollServices.IOService.DB_IO;

public class EmployeePayrollTestCase {
    @Test
    public void givenEmployeePayrollInDB_whenRetrieved_ShouldMatchEmployeeCount() throws InvalidException {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        List<EmployeePayrollData> employeePayrollData = employeePayrollServices.readEmployeeData(DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_whenUpdated_ShouldMatchWithDB() throws InvalidException {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        employeePayrollServices.updateEmployeeSalary("Terisa", 10000.00);
        boolean result = employeePayrollServices.checkEmployeeDataSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    @Test
    public void givenDateRange_whenRetrieved_ShouldSyncWithDB() throws InvalidException {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollServices.readEmployeeDataForDateRange(DB_IO, startDate, endDate);
        Assert.assertEquals(3, employeePayrollDataList.size());
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperValue() throws InvalidException {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        Map<String, Double> averageSalaryByGender = employeePayrollServices.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("M").equals(20000.00) &&
                averageSalaryByGender.get("F").equals(30000.00));
    }

    @Test
    public void givenNewEmployee_whenAdded_shouldSyncWithDB() {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        employeePayrollServices.addEmployeeToPayroll("Mark", 50000.00, LocalDate.now(), "M",DB_IO);
        boolean result = employeePayrollServices.checkEmployeeDataSyncWithDB("Mark");
        Assert.assertTrue(result);
    }

}
