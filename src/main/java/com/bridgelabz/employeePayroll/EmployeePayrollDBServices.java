package com.bridgelabz.employeePayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBServices {
    private static EmployeePayrollDBServices employeePayrollDBServices;

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/EMPLOYEE_SERVICE?useSSL=false";
        String username = "root";
        String password = "root";
        Connection connection;
        connection = DriverManager.getConnection(jdbcURL, username, password);
        return connection;
    }

    public static EmployeePayrollDBServices getInstance() {
        if (employeePayrollDBServices == null)
            employeePayrollDBServices = new EmployeePayrollDBServices();
        return employeePayrollDBServices;
    }

    public List<EmployeePayrollData> getEmployeeDataUsingDB(String sql) throws InvalidException {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
            return employeePayrollDataList;
        } catch (Exception exception) {
            throw new InvalidException("JDBC_QUERY_WRONG",
                    InvalidException.ExceptionType.SQL_EXCEPTION);
        }
    }

    public List<EmployeePayrollData> readData() throws InvalidException {
        String sql = " SELECT * FROM EMPLOYEE; ";
        return this.getEmployeeDataUsingDB(sql);
    }
}
