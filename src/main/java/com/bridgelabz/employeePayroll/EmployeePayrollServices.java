 package com.bridgelabz.employeePayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollServices {

    public List<EmployeePayrollData> employeePayrollList;
    public EmployeePayrollDBServices employeePayrollDBServices;


    public enum IOService {
        DB_IO, FILE_IO
    }

    public void printData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFIleIOService().printData();
        else
            System.out.println(employeePayrollList);

    }


    public boolean deleteEmployeeData(String name, IOService ioService) {
        boolean is_active = true;
        if (ioService.equals(IOService.DB_IO)) {
            is_active = employeePayrollDBServices.deleteData(name);
        }
        return is_active;
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFIleIOService().countEntries();
        return employeePayrollList.size();
    }


    public EmployeePayrollServices() {
        employeePayrollDBServices = EmployeePayrollDBServices.getInstance();
    }

    public List<EmployeePayrollData> readEmployeeData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBServices.readData();
        return this.employeePayrollList;
    }

    public int updateEmployeeSalary(String name, double salary, IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            int i = employeePayrollDBServices.updateData(name, salary);
            return i;
        }
        return 0;
    }

    public boolean checkEmployeeDataSyncWithDB(String name) {
        String sql = "select * from employee where name= ?";
        String fetchedName = null;
        try (Connection connection = employeePayrollDBServices.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fetchedName = rs.getString("NAME");
                if (fetchedName.equalsIgnoreCase(name))
                    return true;
                else
                    return false;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public List<EmployeePayrollData> readEmployeeDataForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDBServices.getEmployeeForDateRange(startDate, endDate);
        return null;
    }

    public Map<String, Double> readAverageSalaryByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDBServices.getAverageSalaryByGender();
        return null;
    }


    public EmployeePayrollData getEmployeeData(String name) {
        EmployeePayrollData employeePayrollData;
        employeePayrollData = this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
        return employeePayrollData;
    }


}
