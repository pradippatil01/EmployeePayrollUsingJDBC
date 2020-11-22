package com.bridgelabz.employeePayroll;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import static com.bridgelabz.employeePayroll.EmployeePayrollServices.IOService.DB_IO;

public class EmployeePayrollTestCase {
    @Test
    public void givenEmployeePayrollInDB_whenRetrieved_ShouldMatchEmployeeCount() throws InvalidException {
        EmployeePayrollServices employeePayrollServices = new EmployeePayrollServices();
        List<EmployeePayrollData> employeePayrollData = employeePayrollServices.readEmployeeData(DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }
}
