package com.bridgelabz.employeePayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBServices {
    private PreparedStatement employeePayrollDataStatement;
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

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollDataList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) throws SQLException {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double salary = resultSet.getDouble("salary");
            LocalDate startDate = resultSet.getDate("start").toLocalDate();
            employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate));
        }
        return employeePayrollDataList;
    }


    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from employee where name= ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    public List<EmployeePayrollData> getEmployeeDataUsingDB(String sql) throws InvalidException {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollDataList = this.getEmployeePayrollData(resultSet);
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

    public int updateData(String name, double salary) throws InvalidException {
        return this.updateDataUsingStatement(name, salary);
    }

    private int updateDataUsingStatement(String name, double salary) throws InvalidException {
        String sql = String.format("update employee set salary=%.2f where name='%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throw new InvalidException("JDBC_QUERY_WRONG",
                    InvalidException.ExceptionType.SQL_EXCEPTION);
        }
    }

    public List<EmployeePayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) throws InvalidException {
        String sql = String.format("SELECT * FROM EMPLOYEE WHERE START BETWEEN '%S' AND '%S';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeeDataUsingDB(sql);

    }

}

