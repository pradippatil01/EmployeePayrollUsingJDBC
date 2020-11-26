package com.bridgelabz.employeePayroll;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.employeePayroll.EmployeePayrollServices.IOService.DB_IO;


public class EmployeePayrollServiceTest {
    @Test
    public void givenEmployeePayrollInDB_whenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        List<EmployeePayrollData> employeePayrollData = employeePayrollServices.readEmployeeData(DB_IO);
        Assert.assertEquals(1, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_whenUpdated_ShouldMatchWithDB() {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        int i = employeePayrollServices.updateEmployeeSalary("MARK", 50000.00, DB_IO);
        Assert.assertEquals(1, i);
    }

    @Test
    public void givenDateRange_whenRetrieved_ShouldSyncWithDB() {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        employeePayrollServices.readEmployeeData(DB_IO);
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollServices.readEmployeeDataForDateRange(DB_IO, startDate, endDate);
        System.out.println(employeePayrollDataList.toString());
        Assert.assertEquals(1, employeePayrollDataList.size());
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperValue() {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        Map<String, Double> averageSalaryByGender = employeePayrollServices.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("M").equals(50000.00) ||
                averageSalaryByGender.get("F").equals(0.0));
    }

    @Test
    public void givenNewEmployee_whenAdded_shouldSyncWithDB() {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        employeePayrollServices.addEmployeeToPayroll("Neeta", 50000.00, LocalDate.now(), "F");
        boolean result = employeePayrollServices.checkEmployeeDataSyncWithDB("Neeta");
        Assert.assertTrue(result);
    }

    @Test
    public void givenEmployeeDB_whenDeleteData_shouldSyncWithDB() {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        boolean is_active = employeePayrollServices.deleteEmployeeData("Neeta", DB_IO);
        Assert.assertFalse(is_active);
    }





}