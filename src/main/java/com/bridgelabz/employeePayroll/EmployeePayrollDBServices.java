package com.bridgelabz.employeePayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Double> getAverageSalaryByGender() throws InvalidException {
        String sql = "select gender,avg(salary) as avg_salary from employee group by gender;";
        Map<String, Double> genderAverageSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("avg_salary");
                genderAverageSalaryMap.put(gender, salary);
            }
        } catch (SQLException exception) {
            throw new InvalidException("JDBC_QUERY_WRONG",
                    InvalidException.ExceptionType.SQL_EXCEPTION);
        }
        return genderAverageSalaryMap;
    }

    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate date, String gender) {
        int employee_id = -1;
        EmployeePayrollData employeePayrollData = null;
        String sql = String.format("Insert into employee(name,gender,salary,start)" +
                "values('%s','%s',%s,'%s')", name, gender, salary, Date.valueOf(date));
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) employee_id = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData(employee_id, name, salary, date);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return employeePayrollData;
    }

}

